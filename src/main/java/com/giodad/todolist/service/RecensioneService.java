package com.giodad.todolist.service;

import com.giodad.todolist.domain.Recensione;
import com.giodad.todolist.repository.RecensioneRepository;
import com.giodad.todolist.service.dto.RecensioneDTO;
import com.giodad.todolist.service.mapper.RecensioneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.giodad.todolist.domain.Recensione}.
 */
@Service
@Transactional
public class RecensioneService {

    private static final Logger LOG = LoggerFactory.getLogger(RecensioneService.class);

    private final RecensioneRepository recensioneRepository;

    private final RecensioneMapper recensioneMapper;

    public RecensioneService(RecensioneRepository recensioneRepository, RecensioneMapper recensioneMapper) {
        this.recensioneRepository = recensioneRepository;
        this.recensioneMapper = recensioneMapper;
    }

    /**
     * Save a recensione.
     *
     * @param recensioneDTO the entity to save.
     * @return the persisted entity.
     */
    public RecensioneDTO save(RecensioneDTO recensioneDTO) {
        LOG.debug("Request to save Recensione : {}", recensioneDTO);
        Recensione recensione = recensioneMapper.toEntity(recensioneDTO);
        recensione = recensioneRepository.save(recensione);
        return recensioneMapper.toDto(recensione);
    }

    /**
     * Update a recensione.
     *
     * @param recensioneDTO the entity to save.
     * @return the persisted entity.
     */
    public RecensioneDTO update(RecensioneDTO recensioneDTO) {
        LOG.debug("Request to update Recensione : {}", recensioneDTO);
        Recensione recensione = recensioneMapper.toEntity(recensioneDTO);
        recensione = recensioneRepository.save(recensione);
        return recensioneMapper.toDto(recensione);
    }

    /**
     * Partially update a recensione.
     *
     * @param recensioneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecensioneDTO> partialUpdate(RecensioneDTO recensioneDTO) {
        LOG.debug("Request to partially update Recensione : {}", recensioneDTO);

        return recensioneRepository
            .findById(recensioneDTO.getId())
            .map(existingRecensione -> {
                recensioneMapper.partialUpdate(existingRecensione, recensioneDTO);

                return existingRecensione;
            })
            .map(recensioneRepository::save)
            .map(recensioneMapper::toDto);
    }

    /**
     * Get all the recensiones with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RecensioneDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recensioneRepository.findAllWithEagerRelationships(pageable).map(recensioneMapper::toDto);
    }

    /**
     * Get one recensione by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecensioneDTO> findOne(Long id) {
        LOG.debug("Request to get Recensione : {}", id);
        return recensioneRepository.findOneWithEagerRelationships(id).map(recensioneMapper::toDto);
    }

    /**
     * Delete the recensione by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Recensione : {}", id);
        recensioneRepository.deleteById(id);
    }
}
