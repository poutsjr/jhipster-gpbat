package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.DetailCommande;
import fr.kearis.gpbat.admin.repository.DetailCommandeRepository;
import fr.kearis.gpbat.admin.repository.search.DetailCommandeSearchRepository;
import fr.kearis.gpbat.admin.service.dto.DetailCommandeDTO;
import fr.kearis.gpbat.admin.service.mapper.DetailCommandeMapper;
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
 * Service Implementation for managing DetailCommande.
 */
@Service
@Transactional
public class DetailCommandeService {

    private final Logger log = LoggerFactory.getLogger(DetailCommandeService.class);
    
    @Inject
    private DetailCommandeRepository detailCommandeRepository;

    @Inject
    private DetailCommandeMapper detailCommandeMapper;

    @Inject
    private DetailCommandeSearchRepository detailCommandeSearchRepository;

    /**
     * Save a detailCommande.
     *
     * @param detailCommandeDTO the entity to save
     * @return the persisted entity
     */
    public DetailCommandeDTO save(DetailCommandeDTO detailCommandeDTO) {
        log.debug("Request to save DetailCommande : {}", detailCommandeDTO);
        DetailCommande detailCommande = detailCommandeMapper.detailCommandeDTOToDetailCommande(detailCommandeDTO);
        detailCommande = detailCommandeRepository.save(detailCommande);
        DetailCommandeDTO result = detailCommandeMapper.detailCommandeToDetailCommandeDTO(detailCommande);
        detailCommandeSearchRepository.save(detailCommande);
        return result;
    }

    /**
     *  Get all the detailCommandes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DetailCommandeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DetailCommandes");
        Page<DetailCommande> result = detailCommandeRepository.findAll(pageable);
        return result.map(detailCommande -> detailCommandeMapper.detailCommandeToDetailCommandeDTO(detailCommande));
    }

    /**
     *  Get one detailCommande by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DetailCommandeDTO findOne(Long id) {
        log.debug("Request to get DetailCommande : {}", id);
        DetailCommande detailCommande = detailCommandeRepository.findOne(id);
        DetailCommandeDTO detailCommandeDTO = detailCommandeMapper.detailCommandeToDetailCommandeDTO(detailCommande);
        return detailCommandeDTO;
    }

    /**
     *  Delete the  detailCommande by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DetailCommande : {}", id);
        detailCommandeRepository.delete(id);
        detailCommandeSearchRepository.delete(id);
    }

    /**
     * Search for the detailCommande corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DetailCommandeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DetailCommandes for query {}", query);
        Page<DetailCommande> result = detailCommandeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(detailCommande -> detailCommandeMapper.detailCommandeToDetailCommandeDTO(detailCommande));
    }
}
