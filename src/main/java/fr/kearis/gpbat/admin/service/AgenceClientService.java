package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.AgenceClient;
import fr.kearis.gpbat.admin.repository.AgenceClientRepository;
import fr.kearis.gpbat.admin.repository.search.AgenceClientSearchRepository;
import fr.kearis.gpbat.admin.service.dto.AgenceClientDTO;
import fr.kearis.gpbat.admin.service.mapper.AgenceClientMapper;
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
 * Service Implementation for managing AgenceClient.
 */
@Service
@Transactional
public class AgenceClientService {

    private final Logger log = LoggerFactory.getLogger(AgenceClientService.class);
    
    @Inject
    private AgenceClientRepository agenceClientRepository;

    @Inject
    private AgenceClientMapper agenceClientMapper;

    @Inject
    private AgenceClientSearchRepository agenceClientSearchRepository;

    /**
     * Save a agenceClient.
     *
     * @param agenceClientDTO the entity to save
     * @return the persisted entity
     */
    public AgenceClientDTO save(AgenceClientDTO agenceClientDTO) {
        log.debug("Request to save AgenceClient : {}", agenceClientDTO);
        AgenceClient agenceClient = agenceClientMapper.agenceClientDTOToAgenceClient(agenceClientDTO);
        agenceClient = agenceClientRepository.save(agenceClient);
        AgenceClientDTO result = agenceClientMapper.agenceClientToAgenceClientDTO(agenceClient);
        agenceClientSearchRepository.save(agenceClient);
        return result;
    }

    /**
     *  Get all the agenceClients.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AgenceClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgenceClients");
        Page<AgenceClient> result = agenceClientRepository.findAll(pageable);
        return result.map(agenceClient -> agenceClientMapper.agenceClientToAgenceClientDTO(agenceClient));
    }

    /**
     *  Get one agenceClient by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AgenceClientDTO findOne(Long id) {
        log.debug("Request to get AgenceClient : {}", id);
        AgenceClient agenceClient = agenceClientRepository.findOne(id);
        AgenceClientDTO agenceClientDTO = agenceClientMapper.agenceClientToAgenceClientDTO(agenceClient);
        return agenceClientDTO;
    }

    /**
     *  Delete the  agenceClient by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AgenceClient : {}", id);
        agenceClientRepository.delete(id);
        agenceClientSearchRepository.delete(id);
    }

    /**
     * Search for the agenceClient corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AgenceClientDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AgenceClients for query {}", query);
        Page<AgenceClient> result = agenceClientSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(agenceClient -> agenceClientMapper.agenceClientToAgenceClientDTO(agenceClient));
    }
}
