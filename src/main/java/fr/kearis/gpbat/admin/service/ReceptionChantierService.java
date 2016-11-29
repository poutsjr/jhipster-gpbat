package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.ReceptionChantier;
import fr.kearis.gpbat.admin.repository.ReceptionChantierRepository;
import fr.kearis.gpbat.admin.repository.search.ReceptionChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ReceptionChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.ReceptionChantierMapper;
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
 * Service Implementation for managing ReceptionChantier.
 */
@Service
@Transactional
public class ReceptionChantierService {

    private final Logger log = LoggerFactory.getLogger(ReceptionChantierService.class);
    
    @Inject
    private ReceptionChantierRepository receptionChantierRepository;

    @Inject
    private ReceptionChantierMapper receptionChantierMapper;

    @Inject
    private ReceptionChantierSearchRepository receptionChantierSearchRepository;

    /**
     * Save a receptionChantier.
     *
     * @param receptionChantierDTO the entity to save
     * @return the persisted entity
     */
    public ReceptionChantierDTO save(ReceptionChantierDTO receptionChantierDTO) {
        log.debug("Request to save ReceptionChantier : {}", receptionChantierDTO);
        ReceptionChantier receptionChantier = receptionChantierMapper.receptionChantierDTOToReceptionChantier(receptionChantierDTO);
        receptionChantier = receptionChantierRepository.save(receptionChantier);
        ReceptionChantierDTO result = receptionChantierMapper.receptionChantierToReceptionChantierDTO(receptionChantier);
        receptionChantierSearchRepository.save(receptionChantier);
        return result;
    }

    /**
     *  Get all the receptionChantiers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ReceptionChantierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReceptionChantiers");
        Page<ReceptionChantier> result = receptionChantierRepository.findAll(pageable);
        return result.map(receptionChantier -> receptionChantierMapper.receptionChantierToReceptionChantierDTO(receptionChantier));
    }

    /**
     *  Get one receptionChantier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ReceptionChantierDTO findOne(Long id) {
        log.debug("Request to get ReceptionChantier : {}", id);
        ReceptionChantier receptionChantier = receptionChantierRepository.findOne(id);
        ReceptionChantierDTO receptionChantierDTO = receptionChantierMapper.receptionChantierToReceptionChantierDTO(receptionChantier);
        return receptionChantierDTO;
    }

    /**
     *  Delete the  receptionChantier by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReceptionChantier : {}", id);
        receptionChantierRepository.delete(id);
        receptionChantierSearchRepository.delete(id);
    }

    /**
     * Search for the receptionChantier corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReceptionChantierDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReceptionChantiers for query {}", query);
        Page<ReceptionChantier> result = receptionChantierSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(receptionChantier -> receptionChantierMapper.receptionChantierToReceptionChantierDTO(receptionChantier));
    }
}
