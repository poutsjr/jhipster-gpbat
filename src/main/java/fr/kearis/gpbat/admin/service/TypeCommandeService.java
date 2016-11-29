package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.TypeCommande;
import fr.kearis.gpbat.admin.repository.TypeCommandeRepository;
import fr.kearis.gpbat.admin.repository.search.TypeCommandeSearchRepository;
import fr.kearis.gpbat.admin.service.dto.TypeCommandeDTO;
import fr.kearis.gpbat.admin.service.mapper.TypeCommandeMapper;
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
 * Service Implementation for managing TypeCommande.
 */
@Service
@Transactional
public class TypeCommandeService {

    private final Logger log = LoggerFactory.getLogger(TypeCommandeService.class);
    
    @Inject
    private TypeCommandeRepository typeCommandeRepository;

    @Inject
    private TypeCommandeMapper typeCommandeMapper;

    @Inject
    private TypeCommandeSearchRepository typeCommandeSearchRepository;

    /**
     * Save a typeCommande.
     *
     * @param typeCommandeDTO the entity to save
     * @return the persisted entity
     */
    public TypeCommandeDTO save(TypeCommandeDTO typeCommandeDTO) {
        log.debug("Request to save TypeCommande : {}", typeCommandeDTO);
        TypeCommande typeCommande = typeCommandeMapper.typeCommandeDTOToTypeCommande(typeCommandeDTO);
        typeCommande = typeCommandeRepository.save(typeCommande);
        TypeCommandeDTO result = typeCommandeMapper.typeCommandeToTypeCommandeDTO(typeCommande);
        typeCommandeSearchRepository.save(typeCommande);
        return result;
    }

    /**
     *  Get all the typeCommandes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TypeCommandeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeCommandes");
        Page<TypeCommande> result = typeCommandeRepository.findAll(pageable);
        return result.map(typeCommande -> typeCommandeMapper.typeCommandeToTypeCommandeDTO(typeCommande));
    }

    /**
     *  Get one typeCommande by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TypeCommandeDTO findOne(Long id) {
        log.debug("Request to get TypeCommande : {}", id);
        TypeCommande typeCommande = typeCommandeRepository.findOne(id);
        TypeCommandeDTO typeCommandeDTO = typeCommandeMapper.typeCommandeToTypeCommandeDTO(typeCommande);
        return typeCommandeDTO;
    }

    /**
     *  Delete the  typeCommande by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeCommande : {}", id);
        typeCommandeRepository.delete(id);
        typeCommandeSearchRepository.delete(id);
    }

    /**
     * Search for the typeCommande corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TypeCommandeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeCommandes for query {}", query);
        Page<TypeCommande> result = typeCommandeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(typeCommande -> typeCommandeMapper.typeCommandeToTypeCommandeDTO(typeCommande));
    }
}
