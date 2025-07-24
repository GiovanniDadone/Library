package com.giodad.todolist.web.rest;

import static com.giodad.todolist.domain.AutoreAsserts.*;
import static com.giodad.todolist.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giodad.todolist.IntegrationTest;
import com.giodad.todolist.domain.Autore;
import com.giodad.todolist.repository.AutoreRepository;
import com.giodad.todolist.service.dto.AutoreDTO;
import com.giodad.todolist.service.mapper.AutoreMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AutoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AutoreResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/autores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AutoreRepository autoreRepository;

    @Autowired
    private AutoreMapper autoreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutoreMockMvc;

    private Autore autore;

    private Autore insertedAutore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autore createEntity() {
        return new Autore().nome(DEFAULT_NOME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autore createUpdatedEntity() {
        return new Autore().nome(UPDATED_NOME);
    }

    @BeforeEach
    void initTest() {
        autore = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAutore != null) {
            autoreRepository.delete(insertedAutore);
            insertedAutore = null;
        }
    }

    @Test
    @Transactional
    void createAutore() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Autore
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);
        var returnedAutoreDTO = om.readValue(
            restAutoreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoreDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AutoreDTO.class
        );

        // Validate the Autore in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAutore = autoreMapper.toEntity(returnedAutoreDTO);
        assertAutoreUpdatableFieldsEquals(returnedAutore, getPersistedAutore(returnedAutore));

        insertedAutore = returnedAutore;
    }

    @Test
    @Transactional
    void createAutoreWithExistingId() throws Exception {
        // Create the Autore with an existing ID
        autore.setId(1L);
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Autore in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autore.setNome(null);

        // Create the Autore, which fails.
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);

        restAutoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAutores() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        // Get all the autoreList
        restAutoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autore.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getAutore() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        // Get the autore
        restAutoreMockMvc
            .perform(get(ENTITY_API_URL_ID, autore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autore.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getAutoresByIdFiltering() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        Long id = autore.getId();

        defaultAutoreFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAutoreFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAutoreFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAutoresByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        // Get all the autoreList where nome equals to
        defaultAutoreFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAutoresByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        // Get all the autoreList where nome in
        defaultAutoreFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAutoresByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        // Get all the autoreList where nome is not null
        defaultAutoreFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllAutoresByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        // Get all the autoreList where nome contains
        defaultAutoreFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAutoresByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        // Get all the autoreList where nome does not contain
        defaultAutoreFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    private void defaultAutoreFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAutoreShouldBeFound(shouldBeFound);
        defaultAutoreShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutoreShouldBeFound(String filter) throws Exception {
        restAutoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autore.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));

        // Check, that the count call also returns 1
        restAutoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutoreShouldNotBeFound(String filter) throws Exception {
        restAutoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAutore() throws Exception {
        // Get the autore
        restAutoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAutore() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autore
        Autore updatedAutore = autoreRepository.findById(autore.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAutore are not directly saved in db
        em.detach(updatedAutore);
        updatedAutore.nome(UPDATED_NOME);
        AutoreDTO autoreDTO = autoreMapper.toDto(updatedAutore);

        restAutoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autoreDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Autore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAutoreToMatchAllProperties(updatedAutore);
    }

    @Test
    @Transactional
    void putNonExistingAutore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autore.setId(longCount.incrementAndGet());

        // Create the Autore
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autoreDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAutore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autore.setId(longCount.incrementAndGet());

        // Create the Autore
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAutore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autore.setId(longCount.incrementAndGet());

        // Create the Autore
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAutoreWithPatch() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autore using partial update
        Autore partialUpdatedAutore = new Autore();
        partialUpdatedAutore.setId(autore.getId());

        restAutoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutore))
            )
            .andExpect(status().isOk());

        // Validate the Autore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutoreUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAutore, autore), getPersistedAutore(autore));
    }

    @Test
    @Transactional
    void fullUpdateAutoreWithPatch() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autore using partial update
        Autore partialUpdatedAutore = new Autore();
        partialUpdatedAutore.setId(autore.getId());

        partialUpdatedAutore.nome(UPDATED_NOME);

        restAutoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutore))
            )
            .andExpect(status().isOk());

        // Validate the Autore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutoreUpdatableFieldsEquals(partialUpdatedAutore, getPersistedAutore(partialUpdatedAutore));
    }

    @Test
    @Transactional
    void patchNonExistingAutore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autore.setId(longCount.incrementAndGet());

        // Create the Autore
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, autoreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAutore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autore.setId(longCount.incrementAndGet());

        // Create the Autore
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAutore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autore.setId(longCount.incrementAndGet());

        // Create the Autore
        AutoreDTO autoreDTO = autoreMapper.toDto(autore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(autoreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAutore() throws Exception {
        // Initialize the database
        insertedAutore = autoreRepository.saveAndFlush(autore);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the autore
        restAutoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, autore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return autoreRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Autore getPersistedAutore(Autore autore) {
        return autoreRepository.findById(autore.getId()).orElseThrow();
    }

    protected void assertPersistedAutoreToMatchAllProperties(Autore expectedAutore) {
        assertAutoreAllPropertiesEquals(expectedAutore, getPersistedAutore(expectedAutore));
    }

    protected void assertPersistedAutoreToMatchUpdatableProperties(Autore expectedAutore) {
        assertAutoreAllUpdatablePropertiesEquals(expectedAutore, getPersistedAutore(expectedAutore));
    }
}
