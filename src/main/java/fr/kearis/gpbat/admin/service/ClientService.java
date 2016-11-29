package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.Client;
import fr.kearis.gpbat.admin.repository.ClientRepository;
import fr.kearis.gpbat.admin.repository.search.ClientSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ClientDTO;
import fr.kearis.gpbat.admin.service.mapper.ClientMapper;
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
 * Service Implementation for managing Client.
 */
@Service
@Transactional
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);
    
    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientMapper clientMapper;

    @Inject
    private ClientSearchRepository clientSearchRepository;

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save
     * @return the persisted entity
     */
    public ClientDTO save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        Client client = clientMapper.clientDTOToClient(clientDTO);
        client = clientRepository.save(client);
        ClientDTO result = clientMapper.clientToClientDTO(client);
        clientSearchRepository.save(client);
        return result;
    }

    /**
     *  Get all the clients.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        Page<Client> result = clientRepository.findAll(pageable);
        return result.map(client -> clientMapper.clientToClientDTO(client));
    }

    /**
     *  Get one client by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ClientDTO findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        Client client = clientRepository.findOne(id);
        ClientDTO clientDTO = clientMapper.clientToClientDTO(client);
        return clientDTO;
    }

    /**
     *  Delete the  client by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.delete(id);
        clientSearchRepository.delete(id);
    }

    /**
     * Search for the client corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Clients for query {}", query);
        Page<Client> result = clientSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(client -> clientMapper.clientToClientDTO(client));
    }
}
