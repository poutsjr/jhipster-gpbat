package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.AvancementChantier;
import fr.kearis.gpbat.admin.repository.AvancementChantierRepository;
import fr.kearis.gpbat.admin.repository.search.AvancementChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.AvancementChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.AvancementChantierMapper;
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
 * Service Implementation for managing AvancementChantier.
 */
@Service
@Transactional
public class AvancementChantierService {

    private final Logger log = LoggerFactory.getLogger(AvancementChantierService.class);
    
    @Inject
    private AvancementChantierRepository avancementChantierRepository;

    @Inject
    private AvancementChantierMapper avancementChantierMapper;

    @Inject
    private AvancementChantierSearchRepository avancementChantierSearchRepository;

    /**
     * Save a avancementChantier.
     *
     * @param avancementChantierDTO the entity to save
     * @return the persisted entity
     */
    public AvancementChantierDTO save(AvancementChantierDTO avancementChantierDTO) {
        log.debug("Request to save AvancementChantier : {}", avancementChantierDTO);
        AvancementChantier avancementChantier = avancementChantierMapper.avancementChantierDTOToAvancementChantier(avancementChantierDTO);
        avancementChantier = avancementChantierRepository.save(avancementChantier);
        AvancementChantierDTO result = avancementChantierMapper.avancementChantierToAvancementChantierDTO(avancementChantier);
        avancementChantierSearchRepository.save(avancementChantier);
        return result;
    }

    /**
     *  Get all the avancementChantiers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AvancementChantierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AvancementChantiers");
        Page<AvancementChantier> result = avancementChantierRepository.findAll(pageable);
        return result.map(avancementChantier -> avancementChantierMapper.avancementChantierToAvancementChantierDTO(avancementChantier));
    }

    /**
     *  Get one avancementChantier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AvancementChantierDTO findOne(Long id) {
        log.debug("Request to get AvancementChantier : {}", id);
        AvancementChantier avancementChantier = avancementChantierRepository.findOne(id);
        AvancementChantierDTO avancementChantierDTO = avancementChantierMapper.avancementChantierToAvancementChantierDTO(avancementChantier);
        return avancementChantierDTO;
    }

    /**
     *  Delete the  avancementChantier by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AvancementChantier : {}", id);
        avancementChantierRepository.delete(id);
        avancementChantierSearchRepository.delete(id);
    }

    /**
     * Search for the avancementChantier corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AvancementChantierDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AvancementChantiers for query {}", query);
        Page<AvancementChantier> result = avancementChantierSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(avancementChantier -> avancementChantierMapper.avancementChantierToAvancementChantierDTO(avancementChantier));
    }
}
