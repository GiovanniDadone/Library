package com.giodad.todolist.web.rest;

import static com.giodad.todolist.domain.LibroAsserts.*;
import static com.giodad.todolist.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giodad.todolist.IntegrationTest;
import com.giodad.todolist.domain.Autore;
import com.giodad.todolist.domain.Libro;
import com.giodad.todolist.repository.LibroRepository;
import com.giodad.todolist.service.dto.LibroDTO;
import com.giodad.todolist.service.mapper.LibroMapper;
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
 * Integration tests for the {@link LibroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LibroResourceIT {

    private static final String DEFAULT_TITOLO = "AAAAAAAAAA";
    private static final String UPDATED_TITOLO = "BBBBBBBBBB";

    private static final Double DEFAULT_PREZZO = 1D;
    private static final Double UPDATED_PREZZO = 2D;
    private static final Double SMALLER_PREZZO = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/libros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private LibroMapper libroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibroMockMvc;

    private Libro libro;

    private Libro insertedLibro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createEntity(EntityManager em) {
        Libro libro = new Libro().titolo(DEFAULT_TITOLO).prezzo(DEFAULT_PREZZO);
        // Add required entity
        Autore autore;
        if (TestUtil.findAll(em, Autore.class).isEmpty()) {
            autore = AutoreResourceIT.createEntity();
            em.persist(autore);
            em.flush();
        } else {
            autore = TestUtil.findAll(em, Autore.class).get(0);
        }
        libro.setAutore(autore);
        return libro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createUpdatedEntity(EntityManager em) {
        Libro updatedLibro = new Libro().titolo(UPDATED_TITOLO).prezzo(UPDATED_PREZZO);
        // Add required entity
        Autore autore;
        if (TestUtil.findAll(em, Autore.class).isEmpty()) {
            autore = AutoreResourceIT.createUpdatedEntity();
            em.persist(autore);
            em.flush();
        } else {
            autore = TestUtil.findAll(em, Autore.class).get(0);
        }
        updatedLibro.setAutore(autore);
        return updatedLibro;
    }

    @BeforeEach
    void initTest() {
        libro = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLibro != null) {
            libroRepository.delete(insertedLibro);
            insertedLibro = null;
        }
    }

    @Test
    @Transactional
    void createLibro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);
        var returnedLibroDTO = om.readValue(
            restLibroMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LibroDTO.class
        );

        // Validate the Libro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLibro = libroMapper.toEntity(returnedLibroDTO);
        assertLibroUpdatableFieldsEquals(returnedLibro, getPersistedLibro(returnedLibro));

        insertedLibro = returnedLibro;
    }

    @Test
    @Transactional
    void createLibroWithExistingId() throws Exception {
        // Create the Libro with an existing ID
        libro.setId(1L);
        LibroDTO libroDTO = libroMapper.toDto(libro);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitoloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        libro.setTitolo(null);

        // Create the Libro, which fails.
        LibroDTO libroDTO = libroMapper.toDto(libro);

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrezzoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        libro.setPrezzo(null);

        // Create the Libro, which fails.
        LibroDTO libroDTO = libroMapper.toDto(libro);

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLibros() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libro.getId().intValue())))
            .andExpect(jsonPath("$.[*].titolo").value(hasItem(DEFAULT_TITOLO)))
            .andExpect(jsonPath("$.[*].prezzo").value(hasItem(DEFAULT_PREZZO)));
    }

    @Test
    @Transactional
    void getLibro() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get the libro
        restLibroMockMvc
            .perform(get(ENTITY_API_URL_ID, libro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(libro.getId().intValue()))
            .andExpect(jsonPath("$.titolo").value(DEFAULT_TITOLO))
            .andExpect(jsonPath("$.prezzo").value(DEFAULT_PREZZO));
    }

    @Test
    @Transactional
    void getLibrosByIdFiltering() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        Long id = libro.getId();

        defaultLibroFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLibroFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLibroFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLibrosByTitoloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where titolo equals to
        defaultLibroFiltering("titolo.equals=" + DEFAULT_TITOLO, "titolo.equals=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllLibrosByTitoloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where titolo in
        defaultLibroFiltering("titolo.in=" + DEFAULT_TITOLO + "," + UPDATED_TITOLO, "titolo.in=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllLibrosByTitoloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where titolo is not null
        defaultLibroFiltering("titolo.specified=true", "titolo.specified=false");
    }

    @Test
    @Transactional
    void getAllLibrosByTitoloContainsSomething() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where titolo contains
        defaultLibroFiltering("titolo.contains=" + DEFAULT_TITOLO, "titolo.contains=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllLibrosByTitoloNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where titolo does not contain
        defaultLibroFiltering("titolo.doesNotContain=" + UPDATED_TITOLO, "titolo.doesNotContain=" + DEFAULT_TITOLO);
    }

    @Test
    @Transactional
    void getAllLibrosByPrezzoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where prezzo equals to
        defaultLibroFiltering("prezzo.equals=" + DEFAULT_PREZZO, "prezzo.equals=" + UPDATED_PREZZO);
    }

    @Test
    @Transactional
    void getAllLibrosByPrezzoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where prezzo in
        defaultLibroFiltering("prezzo.in=" + DEFAULT_PREZZO + "," + UPDATED_PREZZO, "prezzo.in=" + UPDATED_PREZZO);
    }

    @Test
    @Transactional
    void getAllLibrosByPrezzoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where prezzo is not null
        defaultLibroFiltering("prezzo.specified=true", "prezzo.specified=false");
    }

    @Test
    @Transactional
    void getAllLibrosByPrezzoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where prezzo is greater than or equal to
        defaultLibroFiltering("prezzo.greaterThanOrEqual=" + DEFAULT_PREZZO, "prezzo.greaterThanOrEqual=" + UPDATED_PREZZO);
    }

    @Test
    @Transactional
    void getAllLibrosByPrezzoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where prezzo is less than or equal to
        defaultLibroFiltering("prezzo.lessThanOrEqual=" + DEFAULT_PREZZO, "prezzo.lessThanOrEqual=" + SMALLER_PREZZO);
    }

    @Test
    @Transactional
    void getAllLibrosByPrezzoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where prezzo is less than
        defaultLibroFiltering("prezzo.lessThan=" + UPDATED_PREZZO, "prezzo.lessThan=" + DEFAULT_PREZZO);
    }

    @Test
    @Transactional
    void getAllLibrosByPrezzoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList where prezzo is greater than
        defaultLibroFiltering("prezzo.greaterThan=" + SMALLER_PREZZO, "prezzo.greaterThan=" + DEFAULT_PREZZO);
    }

    @Test
    @Transactional
    void getAllLibrosByAutoreIsEqualToSomething() throws Exception {
        Autore autore;
        if (TestUtil.findAll(em, Autore.class).isEmpty()) {
            libroRepository.saveAndFlush(libro);
            autore = AutoreResourceIT.createEntity();
        } else {
            autore = TestUtil.findAll(em, Autore.class).get(0);
        }
        em.persist(autore);
        em.flush();
        libro.setAutore(autore);
        libroRepository.saveAndFlush(libro);
        Long autoreId = autore.getId();
        // Get all the libroList where autore equals to autoreId
        defaultLibroShouldBeFound("autoreId.equals=" + autoreId);

        // Get all the libroList where autore equals to (autoreId + 1)
        defaultLibroShouldNotBeFound("autoreId.equals=" + (autoreId + 1));
    }

    private void defaultLibroFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLibroShouldBeFound(shouldBeFound);
        defaultLibroShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLibroShouldBeFound(String filter) throws Exception {
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libro.getId().intValue())))
            .andExpect(jsonPath("$.[*].titolo").value(hasItem(DEFAULT_TITOLO)))
            .andExpect(jsonPath("$.[*].prezzo").value(hasItem(DEFAULT_PREZZO)));

        // Check, that the count call also returns 1
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLibroShouldNotBeFound(String filter) throws Exception {
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLibro() throws Exception {
        // Get the libro
        restLibroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLibro() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libro
        Libro updatedLibro = libroRepository.findById(libro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLibro are not directly saved in db
        em.detach(updatedLibro);
        updatedLibro.titolo(UPDATED_TITOLO).prezzo(UPDATED_PREZZO);
        LibroDTO libroDTO = libroMapper.toDto(updatedLibro);

        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libroDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLibroToMatchAllProperties(updatedLibro);
    }

    @Test
    @Transactional
    void putNonExistingLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libroDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLibroWithPatch() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libro using partial update
        Libro partialUpdatedLibro = new Libro();
        partialUpdatedLibro.setId(libro.getId());

        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibroUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLibro, libro), getPersistedLibro(libro));
    }

    @Test
    @Transactional
    void fullUpdateLibroWithPatch() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libro using partial update
        Libro partialUpdatedLibro = new Libro();
        partialUpdatedLibro.setId(libro.getId());

        partialUpdatedLibro.titolo(UPDATED_TITOLO).prezzo(UPDATED_PREZZO);

        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibroUpdatableFieldsEquals(partialUpdatedLibro, getPersistedLibro(partialUpdatedLibro));
    }

    @Test
    @Transactional
    void patchNonExistingLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, libroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLibro() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the libro
        restLibroMockMvc
            .perform(delete(ENTITY_API_URL_ID, libro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return libroRepository.count();
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

    protected Libro getPersistedLibro(Libro libro) {
        return libroRepository.findById(libro.getId()).orElseThrow();
    }

    protected void assertPersistedLibroToMatchAllProperties(Libro expectedLibro) {
        assertLibroAllPropertiesEquals(expectedLibro, getPersistedLibro(expectedLibro));
    }

    protected void assertPersistedLibroToMatchUpdatableProperties(Libro expectedLibro) {
        assertLibroAllUpdatablePropertiesEquals(expectedLibro, getPersistedLibro(expectedLibro));
    }
}
