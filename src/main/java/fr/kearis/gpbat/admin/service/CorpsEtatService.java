package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.CorpsEtat;
import fr.kearis.gpbat.admin.repository.CorpsEtatRepository;
import fr.kearis.gpbat.admin.repository.search.CorpsEtatSearchRepository;
import fr.kearis.gpbat.admin.service.dto.CorpsEtatDTO;
import fr.kearis.gpbat.admin.service.mapper.CorpsEtatMapper;
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
 * Service Implementation for managing CorpsEtat.
 */
@Service
@Transactional
public class CorpsEtatService {

    private final Logger log = LoggerFactory.getLogger(CorpsEtatService.class);
    
    @Inject
    private CorpsEtatRepository corpsEtatRepository;

    @Inject
    private CorpsEtatMapper corpsEtatMapper;

    @Inject
    private CorpsEtatSearchRepository corpsEtatSearchRepository;

    /**
     * Save a corpsEtat.
     *
     * @param corpsEtatDTO the entity to save
     * @return the persisted entity
     */
    public CorpsEtatDTO save(CorpsEtatDTO corpsEtatDTO) {
        log.debug("Request to save CorpsEtat : {}", corpsEtatDTO);
        CorpsEtat corpsEtat = corpsEtatMapper.corpsEtatDTOToCorpsEtat(corpsEtatDTO);
        corpsEtat = corpsEtatRepository.save(corpsEtat);
        CorpsEtatDTO result = corpsEtatMapper.corpsEtatToCorpsEtatDTO(corpsEtat);
        corpsEtatSearchRepository.save(corpsEtat);
        return result;
    }

    /**
     *  Get all the corpsEtats.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CorpsEtatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CorpsEtats");
        Page<CorpsEtat> result = corpsEtatRepository.findAll(pageable);
        return result.map(corpsEtat -> corpsEtatMapper.corpsEtatToCorpsEtatDTO(corpsEtat));
    }

    /**
     *  Get one corpsEtat by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CorpsEtatDTO findOne(Long id) {
        log.debug("Request to get CorpsEtat : {}", id);
        CorpsEtat corpsEtat = corpsEtatRepository.findOne(id);
        CorpsEtatDTO corpsEtatDTO = corpsEtatMapper.corpsEtatToCorpsEtatDTO(corpsEtat);
        return corpsEtatDTO;
    }

    /**
     *  Delete the  corpsEtat by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CorpsEtat : {}", id);
        corpsEtatRepository.delete(id);
        corpsEtatSearchRepository.delete(id);
    }

    /**
     * Search for the corpsEtat corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CorpsEtatDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CorpsEtats for query {}", query);
        Page<CorpsEtat> result = corpsEtatSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(corpsEtat -> corpsEtatMapper.corpsEtatToCorpsEtatDTO(corpsEtat));
    }
}
