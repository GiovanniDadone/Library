package com.giodad.todolist.service;

import com.giodad.todolist.domain.*; // for static metamodels
import com.giodad.todolist.domain.Autore;
import com.giodad.todolist.repository.AutoreRepository;
import com.giodad.todolist.service.criteria.AutoreCriteria;
import com.giodad.todolist.service.dto.AutoreDTO;
import com.giodad.todolist.service.mapper.AutoreMapper;
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
 * Service for executing complex queries for {@link Autore} entities in the database.
 * The main input is a {@link AutoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AutoreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutoreQueryService extends QueryService<Autore> {

    private static final Logger LOG = LoggerFactory.getLogger(AutoreQueryService.class);

    private final AutoreRepository autoreRepository;

    private final AutoreMapper autoreMapper;

    public AutoreQueryService(AutoreRepository autoreRepository, AutoreMapper autoreMapper) {
        this.autoreRepository = autoreRepository;
        this.autoreMapper = autoreMapper;
    }

    /**
     * Return a {@link Page} of {@link AutoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AutoreDTO> findByCriteria(AutoreCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Autore> specification = createSpecification(criteria);
        return autoreRepository.findAll(specification, page).map(autoreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutoreCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Autore> specification = createSpecification(criteria);
        return autoreRepository.count(specification);
    }

    /**
     * Function to convert {@link AutoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Autore> createSpecification(AutoreCriteria criteria) {
        Specification<Autore> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Autore_.id),
                buildStringSpecification(criteria.getNome(), Autore_.nome),
                buildSpecification(criteria.getLibriId(), root -> root.join(Autore_.libris, JoinType.LEFT).get(Libro_.id))
            );
        }
        return specification;
    }
}
