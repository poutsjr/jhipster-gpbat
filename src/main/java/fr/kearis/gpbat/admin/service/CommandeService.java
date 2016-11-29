package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.Commande;
import fr.kearis.gpbat.admin.repository.CommandeRepository;
import fr.kearis.gpbat.admin.repository.search.CommandeSearchRepository;
import fr.kearis.gpbat.admin.service.dto.CommandeDTO;
import fr.kearis.gpbat.admin.service.mapper.CommandeMapper;
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
 * Service Implementation for managing Commande.
 */
@Service
@Transactional
public class CommandeService {

    private final Logger log = LoggerFactory.getLogger(CommandeService.class);
    
    @Inject
    private CommandeRepository commandeRepository;

    @Inject
    private CommandeMapper commandeMapper;

    @Inject
    private CommandeSearchRepository commandeSearchRepository;

    /**
     * Save a commande.
     *
     * @param commandeDTO the entity to save
     * @return the persisted entity
     */
    public CommandeDTO save(CommandeDTO commandeDTO) {
        log.debug("Request to save Commande : {}", commandeDTO);
        Commande commande = commandeMapper.commandeDTOToCommande(commandeDTO);
        commande = commandeRepository.save(commande);
        CommandeDTO result = commandeMapper.commandeToCommandeDTO(commande);
        commandeSearchRepository.save(commande);
        return result;
    }

    /**
     *  Get all the commandes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CommandeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commandes");
        Page<Commande> result = commandeRepository.findAll(pageable);
        return result.map(commande -> commandeMapper.commandeToCommandeDTO(commande));
    }

    /**
     *  Get one commande by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CommandeDTO findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        Commande commande = commandeRepository.findOne(id);
        CommandeDTO commandeDTO = commandeMapper.commandeToCommandeDTO(commande);
        return commandeDTO;
    }

    /**
     *  Delete the  commande by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        commandeRepository.delete(id);
        commandeSearchRepository.delete(id);
    }

    /**
     * Search for the commande corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommandeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Commandes for query {}", query);
        Page<Commande> result = commandeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(commande -> commandeMapper.commandeToCommandeDTO(commande));
    }
}
