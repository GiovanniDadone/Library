package com.giodad.todolist.web.rest;

import com.giodad.todolist.repository.AutoreRepository;
import com.giodad.todolist.service.AutoreQueryService;
import com.giodad.todolist.service.AutoreService;
import com.giodad.todolist.service.criteria.AutoreCriteria;
import com.giodad.todolist.service.dto.AutoreDTO;
import com.giodad.todolist.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.giodad.todolist.domain.Autore}.
 */
@RestController
@RequestMapping("/api/autores")
public class AutoreResource {

    private static final Logger LOG = LoggerFactory.getLogger(AutoreResource.class);

    private static final String ENTITY_NAME = "autore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutoreService autoreService;

    private final AutoreRepository autoreRepository;

    private final AutoreQueryService autoreQueryService;

    public AutoreResource(AutoreService autoreService, AutoreRepository autoreRepository, AutoreQueryService autoreQueryService) {
        this.autoreService = autoreService;
        this.autoreRepository = autoreRepository;
        this.autoreQueryService = autoreQueryService;
    }

    /**
     * {@code POST  /autores} : Create a new autore.
     *
     * @param autoreDTO the autoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autoreDTO, or with status {@code 400 (Bad Request)} if the autore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AutoreDTO> createAutore(@Valid @RequestBody AutoreDTO autoreDTO) throws URISyntaxException {
        LOG.debug("REST request to save Autore : {}", autoreDTO);
        if (autoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new autore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        autoreDTO = autoreService.save(autoreDTO);
        return ResponseEntity.created(new URI("/api/autores/" + autoreDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, autoreDTO.getId().toString()))
            .body(autoreDTO);
    }

    /**
     * {@code PUT  /autores/:id} : Updates an existing autore.
     *
     * @param id the id of the autoreDTO to save.
     * @param autoreDTO the autoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoreDTO,
     * or with status {@code 400 (Bad Request)} if the autoreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutoreDTO> updateAutore(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AutoreDTO autoreDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Autore : {}, {}", id, autoreDTO);
        if (autoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        autoreDTO = autoreService.update(autoreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, autoreDTO.getId().toString()))
            .body(autoreDTO);
    }

    /**
     * {@code PATCH  /autores/:id} : Partial updates given fields of an existing autore, field will ignore if it is null
     *
     * @param id the id of the autoreDTO to save.
     * @param autoreDTO the autoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoreDTO,
     * or with status {@code 400 (Bad Request)} if the autoreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the autoreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the autoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AutoreDTO> partialUpdateAutore(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AutoreDTO autoreDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Autore partially : {}, {}", id, autoreDTO);
        if (autoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AutoreDTO> result = autoreService.partialUpdate(autoreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, autoreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /autores} : get all the autores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autores in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AutoreDTO>> getAllAutores(
        AutoreCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Autores by criteria: {}", criteria);

        Page<AutoreDTO> page = autoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /autores/count} : count all the autores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAutores(AutoreCriteria criteria) {
        LOG.debug("REST request to count Autores by criteria: {}", criteria);
        return ResponseEntity.ok().body(autoreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /autores/:id} : get the "id" autore.
     *
     * @param id the id of the autoreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutoreDTO> getAutore(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Autore : {}", id);
        Optional<AutoreDTO> autoreDTO = autoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoreDTO);
    }

    /**
     * {@code DELETE  /autores/:id} : delete the "id" autore.
     *
     * @param id the id of the autoreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutore(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Autore : {}", id);
        autoreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
