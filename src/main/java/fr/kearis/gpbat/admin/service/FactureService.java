package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.Facture;
import fr.kearis.gpbat.admin.repository.FactureRepository;
import fr.kearis.gpbat.admin.repository.search.FactureSearchRepository;
import fr.kearis.gpbat.admin.service.dto.FactureDTO;
import fr.kearis.gpbat.admin.service.mapper.FactureMapper;
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
 * Service Implementation for managing Facture.
 */
@Service
@Transactional
public class FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureService.class);
    
    @Inject
    private FactureRepository factureRepository;

    @Inject
    private FactureMapper factureMapper;

    @Inject
    private FactureSearchRepository factureSearchRepository;

    /**
     * Save a facture.
     *
     * @param factureDTO the entity to save
     * @return the persisted entity
     */
    public FactureDTO save(FactureDTO factureDTO) {
        log.debug("Request to save Facture : {}", factureDTO);
        Facture facture = factureMapper.factureDTOToFacture(factureDTO);
        facture = factureRepository.save(facture);
        FactureDTO result = factureMapper.factureToFactureDTO(facture);
        factureSearchRepository.save(facture);
        return result;
    }

    /**
     *  Get all the factures.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<FactureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Factures");
        Page<Facture> result = factureRepository.findAll(pageable);
        return result.map(facture -> factureMapper.factureToFactureDTO(facture));
    }

    /**
     *  Get one facture by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public FactureDTO findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        Facture facture = factureRepository.findOne(id);
        FactureDTO factureDTO = factureMapper.factureToFactureDTO(facture);
        return factureDTO;
    }

    /**
     *  Delete the  facture by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.delete(id);
        factureSearchRepository.delete(id);
    }

    /**
     * Search for the facture corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FactureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Factures for query {}", query);
        Page<Facture> result = factureSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(facture -> factureMapper.factureToFactureDTO(facture));
    }
}
