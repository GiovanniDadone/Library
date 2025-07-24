package com.giodad.todolist.service;

import com.giodad.todolist.domain.Autore;
import com.giodad.todolist.repository.AutoreRepository;
import com.giodad.todolist.service.dto.AutoreDTO;
import com.giodad.todolist.service.mapper.AutoreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.giodad.todolist.domain.Autore}.
 */
@Service
@Transactional
public class AutoreService {

    private static final Logger LOG = LoggerFactory.getLogger(AutoreService.class);

    private final AutoreRepository autoreRepository;

    private final AutoreMapper autoreMapper;

    public AutoreService(AutoreRepository autoreRepository, AutoreMapper autoreMapper) {
        this.autoreRepository = autoreRepository;
        this.autoreMapper = autoreMapper;
    }

    /**
     * Save a autore.
     *
     * @param autoreDTO the entity to save.
     * @return the persisted entity.
     */
    public AutoreDTO save(AutoreDTO autoreDTO) {
        LOG.debug("Request to save Autore : {}", autoreDTO);
        Autore autore = autoreMapper.toEntity(autoreDTO);
        autore = autoreRepository.save(autore);
        return autoreMapper.toDto(autore);
    }

    /**
     * Update a autore.
     *
     * @param autoreDTO the entity to save.
     * @return the persisted entity.
     */
    public AutoreDTO update(AutoreDTO autoreDTO) {
        LOG.debug("Request to update Autore : {}", autoreDTO);
        Autore autore = autoreMapper.toEntity(autoreDTO);
        autore = autoreRepository.save(autore);
        return autoreMapper.toDto(autore);
    }

    /**
     * Partially update a autore.
     *
     * @param autoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AutoreDTO> partialUpdate(AutoreDTO autoreDTO) {
        LOG.debug("Request to partially update Autore : {}", autoreDTO);

        return autoreRepository
            .findById(autoreDTO.getId())
            .map(existingAutore -> {
                autoreMapper.partialUpdate(existingAutore, autoreDTO);

                return existingAutore;
            })
            .map(autoreRepository::save)
            .map(autoreMapper::toDto);
    }

    /**
     * Get one autore by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AutoreDTO> findOne(Long id) {
        LOG.debug("Request to get Autore : {}", id);
        return autoreRepository.findById(id).map(autoreMapper::toDto);
    }

    /**
     * Delete the autore by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Autore : {}", id);
        autoreRepository.deleteById(id);
    }
}
