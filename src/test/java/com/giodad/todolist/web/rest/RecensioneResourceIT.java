package com.giodad.todolist.web.rest;

import static com.giodad.todolist.domain.RecensioneAsserts.*;
import static com.giodad.todolist.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giodad.todolist.IntegrationTest;
import com.giodad.todolist.domain.Libro;
import com.giodad.todolist.domain.Recensione;
import com.giodad.todolist.domain.User;
import com.giodad.todolist.repository.RecensioneRepository;
import com.giodad.todolist.repository.UserRepository;
import com.giodad.todolist.service.RecensioneService;
import com.giodad.todolist.service.dto.RecensioneDTO;
import com.giodad.todolist.service.mapper.RecensioneMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecensioneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecensioneResourceIT {

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recensiones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private RecensioneRepository recensioneRepositoryMock;

    @Autowired
    private RecensioneMapper recensioneMapper;

    @Mock
    private RecensioneService recensioneServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecensioneMockMvc;

    private Recensione recensione;

    private Recensione insertedRecensione;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recensione createEntity(EntityManager em) {
        Recensione recensione = new Recensione().descrizione(DEFAULT_DESCRIZIONE);
        // Add required entity
        Libro libro;
        if (TestUtil.findAll(em, Libro.class).isEmpty()) {
            libro = LibroResourceIT.createEntity(em);
            em.persist(libro);
            em.flush();
        } else {
            libro = TestUtil.findAll(em, Libro.class).get(0);
        }
        recensione.setLibro(libro);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        recensione.setUser(user);
        return recensione;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recensione createUpdatedEntity(EntityManager em) {
        Recensione updatedRecensione = new Recensione().descrizione(UPDATED_DESCRIZIONE);
        // Add required entity
        Libro libro;
        if (TestUtil.findAll(em, Libro.class).isEmpty()) {
            libro = LibroResourceIT.createUpdatedEntity(em);
            em.persist(libro);
            em.flush();
        } else {
            libro = TestUtil.findAll(em, Libro.class).get(0);
        }
        updatedRecensione.setLibro(libro);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedRecensione.setUser(user);
        return updatedRecensione;
    }

    @BeforeEach
    void initTest() {
        recensione = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRecensione != null) {
            recensioneRepository.delete(insertedRecensione);
            insertedRecensione = null;
        }
    }

    @Test
    @Transactional
    void createRecensione() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recensione
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(recensione);
        var returnedRecensioneDTO = om.readValue(
            restRecensioneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recensioneDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecensioneDTO.class
        );

        // Validate the Recensione in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecensione = recensioneMapper.toEntity(returnedRecensioneDTO);
        assertRecensioneUpdatableFieldsEquals(returnedRecensione, getPersistedRecensione(returnedRecensione));

        insertedRecensione = returnedRecensione;
    }

    @Test
    @Transactional
    void createRecensioneWithExistingId() throws Exception {
        // Create the Recensione with an existing ID
        recensione.setId(1L);
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(recensione);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecensioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recensioneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recensione in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecensiones() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        // Get all the recensioneList
        restRecensioneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recensione.getId().intValue())))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecensionesWithEagerRelationshipsIsEnabled() throws Exception {
        when(recensioneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecensioneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recensioneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecensionesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recensioneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecensioneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(recensioneRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRecensione() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        // Get the recensione
        restRecensioneMockMvc
            .perform(get(ENTITY_API_URL_ID, recensione.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recensione.getId().intValue()))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE));
    }

    @Test
    @Transactional
    void getRecensionesByIdFiltering() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        Long id = recensione.getId();

        defaultRecensioneFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecensioneFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecensioneFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecensionesByDescrizioneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        // Get all the recensioneList where descrizione equals to
        defaultRecensioneFiltering("descrizione.equals=" + DEFAULT_DESCRIZIONE, "descrizione.equals=" + UPDATED_DESCRIZIONE);
    }

    @Test
    @Transactional
    void getAllRecensionesByDescrizioneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        // Get all the recensioneList where descrizione in
        defaultRecensioneFiltering(
            "descrizione.in=" + DEFAULT_DESCRIZIONE + "," + UPDATED_DESCRIZIONE,
            "descrizione.in=" + UPDATED_DESCRIZIONE
        );
    }

    @Test
    @Transactional
    void getAllRecensionesByDescrizioneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        // Get all the recensioneList where descrizione is not null
        defaultRecensioneFiltering("descrizione.specified=true", "descrizione.specified=false");
    }

    @Test
    @Transactional
    void getAllRecensionesByDescrizioneContainsSomething() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        // Get all the recensioneList where descrizione contains
        defaultRecensioneFiltering("descrizione.contains=" + DEFAULT_DESCRIZIONE, "descrizione.contains=" + UPDATED_DESCRIZIONE);
    }

    @Test
    @Transactional
    void getAllRecensionesByDescrizioneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        // Get all the recensioneList where descrizione does not contain
        defaultRecensioneFiltering(
            "descrizione.doesNotContain=" + UPDATED_DESCRIZIONE,
            "descrizione.doesNotContain=" + DEFAULT_DESCRIZIONE
        );
    }

    @Test
    @Transactional
    void getAllRecensionesByLibroIsEqualToSomething() throws Exception {
        Libro libro;
        if (TestUtil.findAll(em, Libro.class).isEmpty()) {
            recensioneRepository.saveAndFlush(recensione);
            libro = LibroResourceIT.createEntity(em);
        } else {
            libro = TestUtil.findAll(em, Libro.class).get(0);
        }
        em.persist(libro);
        em.flush();
        recensione.setLibro(libro);
        recensioneRepository.saveAndFlush(recensione);
        Long libroId = libro.getId();
        // Get all the recensioneList where libro equals to libroId
        defaultRecensioneShouldBeFound("libroId.equals=" + libroId);

        // Get all the recensioneList where libro equals to (libroId + 1)
        defaultRecensioneShouldNotBeFound("libroId.equals=" + (libroId + 1));
    }

    @Test
    @Transactional
    void getAllRecensionesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            recensioneRepository.saveAndFlush(recensione);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        recensione.setUser(user);
        recensioneRepository.saveAndFlush(recensione);
        Long userId = user.getId();
        // Get all the recensioneList where user equals to userId
        defaultRecensioneShouldBeFound("userId.equals=" + userId);

        // Get all the recensioneList where user equals to (userId + 1)
        defaultRecensioneShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultRecensioneFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecensioneShouldBeFound(shouldBeFound);
        defaultRecensioneShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecensioneShouldBeFound(String filter) throws Exception {
        restRecensioneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recensione.getId().intValue())))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE)));

        // Check, that the count call also returns 1
        restRecensioneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecensioneShouldNotBeFound(String filter) throws Exception {
        restRecensioneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecensioneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecensione() throws Exception {
        // Get the recensione
        restRecensioneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecensione() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recensione
        Recensione updatedRecensione = recensioneRepository.findById(recensione.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecensione are not directly saved in db
        em.detach(updatedRecensione);
        updatedRecensione.descrizione(UPDATED_DESCRIZIONE);
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(updatedRecensione);

        restRecensioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recensioneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recensioneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recensione in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecensioneToMatchAllProperties(updatedRecensione);
    }

    @Test
    @Transactional
    void putNonExistingRecensione() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensione.setId(longCount.incrementAndGet());

        // Create the Recensione
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(recensione);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecensioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recensioneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recensioneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recensione in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecensione() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensione.setId(longCount.incrementAndGet());

        // Create the Recensione
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(recensione);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecensioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recensioneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recensione in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecensione() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensione.setId(longCount.incrementAndGet());

        // Create the Recensione
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(recensione);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecensioneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recensioneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recensione in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecensioneWithPatch() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recensione using partial update
        Recensione partialUpdatedRecensione = new Recensione();
        partialUpdatedRecensione.setId(recensione.getId());

        restRecensioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecensione.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecensione))
            )
            .andExpect(status().isOk());

        // Validate the Recensione in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecensioneUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecensione, recensione),
            getPersistedRecensione(recensione)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecensioneWithPatch() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recensione using partial update
        Recensione partialUpdatedRecensione = new Recensione();
        partialUpdatedRecensione.setId(recensione.getId());

        partialUpdatedRecensione.descrizione(UPDATED_DESCRIZIONE);

        restRecensioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecensione.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecensione))
            )
            .andExpect(status().isOk());

        // Validate the Recensione in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecensioneUpdatableFieldsEquals(partialUpdatedRecensione, getPersistedRecensione(partialUpdatedRecensione));
    }

    @Test
    @Transactional
    void patchNonExistingRecensione() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensione.setId(longCount.incrementAndGet());

        // Create the Recensione
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(recensione);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecensioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recensioneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recensioneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recensione in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecensione() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensione.setId(longCount.incrementAndGet());

        // Create the Recensione
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(recensione);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecensioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recensioneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recensione in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecensione() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensione.setId(longCount.incrementAndGet());

        // Create the Recensione
        RecensioneDTO recensioneDTO = recensioneMapper.toDto(recensione);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecensioneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recensioneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recensione in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecensione() throws Exception {
        // Initialize the database
        insertedRecensione = recensioneRepository.saveAndFlush(recensione);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recensione
        restRecensioneMockMvc
            .perform(delete(ENTITY_API_URL_ID, recensione.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recensioneRepository.count();
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

    protected Recensione getPersistedRecensione(Recensione recensione) {
        return recensioneRepository.findById(recensione.getId()).orElseThrow();
    }

    protected void assertPersistedRecensioneToMatchAllProperties(Recensione expectedRecensione) {
        assertRecensioneAllPropertiesEquals(expectedRecensione, getPersistedRecensione(expectedRecensione));
    }

    protected void assertPersistedRecensioneToMatchUpdatableProperties(Recensione expectedRecensione) {
        assertRecensioneAllUpdatablePropertiesEquals(expectedRecensione, getPersistedRecensione(expectedRecensione));
    }
}
