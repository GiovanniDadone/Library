package com.giodad.todolist.web.rest;

import com.giodad.todolist.repository.RecensioneRepository;
import com.giodad.todolist.service.RecensioneQueryService;
import com.giodad.todolist.service.RecensioneService;
import com.giodad.todolist.service.criteria.RecensioneCriteria;
import com.giodad.todolist.service.dto.RecensioneDTO;
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
 * REST controller for managing {@link com.giodad.todolist.domain.Recensione}.
 */
@RestController
@RequestMapping("/api/recensiones")
public class RecensioneResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecensioneResource.class);

    private static final String ENTITY_NAME = "recensione";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecensioneService recensioneService;

    private final RecensioneRepository recensioneRepository;

    private final RecensioneQueryService recensioneQueryService;

    public RecensioneResource(
        RecensioneService recensioneService,
        RecensioneRepository recensioneRepository,
        RecensioneQueryService recensioneQueryService
    ) {
        this.recensioneService = recensioneService;
        this.recensioneRepository = recensioneRepository;
        this.recensioneQueryService = recensioneQueryService;
    }

    /**
     * {@code POST  /recensiones} : Create a new recensione.
     *
     * @param recensioneDTO the recensioneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recensioneDTO, or with status {@code 400 (Bad Request)} if the recensione has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecensioneDTO> createRecensione(@Valid @RequestBody RecensioneDTO recensioneDTO) throws URISyntaxException {
        LOG.debug("REST request to save Recensione : {}", recensioneDTO);
        if (recensioneDTO.getId() != null) {
            throw new BadRequestAlertException("A new recensione cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recensioneDTO = recensioneService.save(recensioneDTO);
        return ResponseEntity.created(new URI("/api/recensiones/" + recensioneDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, recensioneDTO.getId().toString()))
            .body(recensioneDTO);
    }

    /**
     * {@code PUT  /recensiones/:id} : Updates an existing recensione.
     *
     * @param id the id of the recensioneDTO to save.
     * @param recensioneDTO the recensioneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recensioneDTO,
     * or with status {@code 400 (Bad Request)} if the recensioneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recensioneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecensioneDTO> updateRecensione(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecensioneDTO recensioneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Recensione : {}, {}", id, recensioneDTO);
        if (recensioneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recensioneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recensioneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recensioneDTO = recensioneService.update(recensioneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recensioneDTO.getId().toString()))
            .body(recensioneDTO);
    }

    /**
     * {@code PATCH  /recensiones/:id} : Partial updates given fields of an existing recensione, field will ignore if it is null
     *
     * @param id the id of the recensioneDTO to save.
     * @param recensioneDTO the recensioneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recensioneDTO,
     * or with status {@code 400 (Bad Request)} if the recensioneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recensioneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recensioneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecensioneDTO> partialUpdateRecensione(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecensioneDTO recensioneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Recensione partially : {}, {}", id, recensioneDTO);
        if (recensioneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recensioneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recensioneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecensioneDTO> result = recensioneService.partialUpdate(recensioneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recensioneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recensiones} : get all the recensiones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recensiones in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecensioneDTO>> getAllRecensiones(
        RecensioneCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Recensiones by criteria: {}", criteria);

        Page<RecensioneDTO> page = recensioneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recensiones/count} : count all the recensiones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRecensiones(RecensioneCriteria criteria) {
        LOG.debug("REST request to count Recensiones by criteria: {}", criteria);
        return ResponseEntity.ok().body(recensioneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recensiones/:id} : get the "id" recensione.
     *
     * @param id the id of the recensioneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recensioneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecensioneDTO> getRecensione(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Recensione : {}", id);
        Optional<RecensioneDTO> recensioneDTO = recensioneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recensioneDTO);
    }

    /**
     * {@code DELETE  /recensiones/:id} : delete the "id" recensione.
     *
     * @param id the id of the recensioneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecensione(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Recensione : {}", id);
        recensioneService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
