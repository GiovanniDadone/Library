package com.giodad.todolist.service;

import com.giodad.todolist.domain.*; // for static metamodels
import com.giodad.todolist.domain.Recensione;
import com.giodad.todolist.repository.RecensioneRepository;
import com.giodad.todolist.service.criteria.RecensioneCriteria;
import com.giodad.todolist.service.dto.RecensioneDTO;
import com.giodad.todolist.service.mapper.RecensioneMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Recensione} entities in the database.
 * The main input is a {@link RecensioneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RecensioneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecensioneQueryService extends QueryService<Recensione> {

    private static final Logger LOG = LoggerFactory.getLogger(RecensioneQueryService.class);

    private final RecensioneRepository recensioneRepository;

    private final RecensioneMapper recensioneMapper;

    public RecensioneQueryService(RecensioneRepository recensioneRepository, RecensioneMapper recensioneMapper) {
        this.recensioneRepository = recensioneRepository;
        this.recensioneMapper = recensioneMapper;
    }

    /**
     * Return a {@link Page} of {@link RecensioneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecensioneDTO> findByCriteria(RecensioneCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Recensione> specification = createSpecification(criteria);
        return recensioneRepository.findAll(specification, page).map(recensioneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecensioneCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Recensione> specification = createSpecification(criteria);
        return recensioneRepository.count(specification);
    }

    /**
     * Function to convert {@link RecensioneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Recensione> createSpecification(RecensioneCriteria criteria) {
        Specification<Recensione> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Recensione_.id),
                buildStringSpecification(criteria.getDescrizione(), Recensione_.descrizione),
                buildSpecification(criteria.getLibroId(), root -> root.join(Recensione_.libro, JoinType.LEFT).get(Libro_.id)),
                buildSpecification(criteria.getUserId(), root -> root.join(Recensione_.user, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
