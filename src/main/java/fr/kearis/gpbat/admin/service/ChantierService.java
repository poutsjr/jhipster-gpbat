package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.Chantier;
import fr.kearis.gpbat.admin.repository.ChantierRepository;
import fr.kearis.gpbat.admin.repository.search.ChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.ChantierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Chantier.
 */
@Service
@Transactional
public class ChantierService {

    private final Logger log = LoggerFactory.getLogger(ChantierService.class);
    
    @Inject
    private ChantierRepository chantierRepository;

    @Inject
    private ChantierMapper chantierMapper;

    @Inject
    private ChantierSearchRepository chantierSearchRepository;

    /**
     * Save a chantier.
     *
     * @param chantierDTO the entity to save
     * @return the persisted entity
     */
    public ChantierDTO save(ChantierDTO chantierDTO) {
        log.debug("Request to save Chantier : {}", chantierDTO);
        Chantier chantier = chantierMapper.chantierDTOToChantier(chantierDTO);
        chantier = chantierRepository.save(chantier);
        ChantierDTO result = chantierMapper.chantierToChantierDTO(chantier);
        chantierSearchRepository.save(chantier);
        return result;
    }

    /**
     *  Get all the chantiers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ChantierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Chantiers");
        Page<Chantier> result = chantierRepository.findAll(pageable);
        return result.map(chantier -> chantierMapper.chantierToChantierDTO(chantier));
    }

    /**
     *  Get one chantier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ChantierDTO findOne(Long id) {
        log.debug("Request to get Chantier : {}", id);
        Chantier chantier = chantierRepository.findOne(id);
        ChantierDTO chantierDTO = chantierMapper.chantierToChantierDTO(chantier);
        return chantierDTO;
    }

    /**
     *  Delete the  chantier by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Chantier : {}", id);
        chantierRepository.delete(id);
        chantierSearchRepository.delete(id);
    }

    /**
     * Search for the chantier corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChantierDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Chantiers for query {}", query);
        Page<Chantier> result = chantierSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(chantier -> chantierMapper.chantierToChantierDTO(chantier));
    }
}
