package com.giodad.todolist.service;

import com.giodad.todolist.domain.*; // for static metamodels
import com.giodad.todolist.domain.Libro;
import com.giodad.todolist.repository.LibroRepository;
import com.giodad.todolist.service.criteria.LibroCriteria;
import com.giodad.todolist.service.dto.LibroDTO;
import com.giodad.todolist.service.mapper.LibroMapper;
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
 * Service for executing complex queries for {@link Libro} entities in the database.
 * The main input is a {@link LibroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LibroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LibroQueryService extends QueryService<Libro> {

    private static final Logger LOG = LoggerFactory.getLogger(LibroQueryService.class);

    private final LibroRepository libroRepository;

    private final LibroMapper libroMapper;

    public LibroQueryService(LibroRepository libroRepository, LibroMapper libroMapper) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
    }

    /**
     * Return a {@link Page} of {@link LibroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LibroDTO> findByCriteria(LibroCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Libro> specification = createSpecification(criteria);
        return libroRepository.findAll(specification, page).map(libroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LibroCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Libro> specification = createSpecification(criteria);
        return libroRepository.count(specification);
    }

    /**
     * Function to convert {@link LibroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Libro> createSpecification(LibroCriteria criteria) {
        Specification<Libro> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Libro_.id),
                buildStringSpecification(criteria.getTitolo(), Libro_.titolo),
                buildRangeSpecification(criteria.getPrezzo(), Libro_.prezzo),
                buildSpecification(criteria.getAutoreId(), root -> root.join(Libro_.autore, JoinType.LEFT).get(Autore_.id)),
                buildSpecification(criteria.getRecensioniId(), root -> root.join(Libro_.recensionis, JoinType.LEFT).get(Recensione_.id))
            );
        }
        return specification;
    }
}
