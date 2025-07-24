package com.giodad.todolist.service;

import com.giodad.todolist.domain.Libro;
import com.giodad.todolist.repository.LibroRepository;
import com.giodad.todolist.service.dto.LibroDTO;
import com.giodad.todolist.service.mapper.LibroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.giodad.todolist.domain.Libro}.
 */
@Service
@Transactional
public class LibroService {

    private static final Logger LOG = LoggerFactory.getLogger(LibroService.class);

    private final LibroRepository libroRepository;

    private final LibroMapper libroMapper;

    public LibroService(LibroRepository libroRepository, LibroMapper libroMapper) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
    }

    /**
     * Save a libro.
     *
     * @param libroDTO the entity to save.
     * @return the persisted entity.
     */
    public LibroDTO save(LibroDTO libroDTO) {
        LOG.debug("Request to save Libro : {}", libroDTO);
        Libro libro = libroMapper.toEntity(libroDTO);
        libro = libroRepository.save(libro);
        return libroMapper.toDto(libro);
    }

    /**
     * Update a libro.
     *
     * @param libroDTO the entity to save.
     * @return the persisted entity.
     */
    public LibroDTO update(LibroDTO libroDTO) {
        LOG.debug("Request to update Libro : {}", libroDTO);
        Libro libro = libroMapper.toEntity(libroDTO);
        libro = libroRepository.save(libro);
        return libroMapper.toDto(libro);
    }

    /**
     * Partially update a libro.
     *
     * @param libroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LibroDTO> partialUpdate(LibroDTO libroDTO) {
        LOG.debug("Request to partially update Libro : {}", libroDTO);

        return libroRepository
            .findById(libroDTO.getId())
            .map(existingLibro -> {
                libroMapper.partialUpdate(existingLibro, libroDTO);

                return existingLibro;
            })
            .map(libroRepository::save)
            .map(libroMapper::toDto);
    }

    /**
     * Get one libro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LibroDTO> findOne(Long id) {
        LOG.debug("Request to get Libro : {}", id);
        return libroRepository.findById(id).map(libroMapper::toDto);
    }

    /**
     * Delete the libro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Libro : {}", id);
        libroRepository.deleteById(id);
    }
}
