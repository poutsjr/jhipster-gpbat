package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.ProduitPartenaire;
import fr.kearis.gpbat.admin.repository.ProduitPartenaireRepository;
import fr.kearis.gpbat.admin.repository.search.ProduitPartenaireSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ProduitPartenaireDTO;
import fr.kearis.gpbat.admin.service.mapper.ProduitPartenaireMapper;
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
 * Service Implementation for managing ProduitPartenaire.
 */
@Service
@Transactional
public class ProduitPartenaireService {

    private final Logger log = LoggerFactory.getLogger(ProduitPartenaireService.class);
    
    @Inject
    private ProduitPartenaireRepository produitPartenaireRepository;

    @Inject
    private ProduitPartenaireMapper produitPartenaireMapper;

    @Inject
    private ProduitPartenaireSearchRepository produitPartenaireSearchRepository;

    /**
     * Save a produitPartenaire.
     *
     * @param produitPartenaireDTO the entity to save
     * @return the persisted entity
     */
    public ProduitPartenaireDTO save(ProduitPartenaireDTO produitPartenaireDTO) {
        log.debug("Request to save ProduitPartenaire : {}", produitPartenaireDTO);
        ProduitPartenaire produitPartenaire = produitPartenaireMapper.produitPartenaireDTOToProduitPartenaire(produitPartenaireDTO);
        produitPartenaire = produitPartenaireRepository.save(produitPartenaire);
        ProduitPartenaireDTO result = produitPartenaireMapper.produitPartenaireToProduitPartenaireDTO(produitPartenaire);
        produitPartenaireSearchRepository.save(produitPartenaire);
        return result;
    }

    /**
     *  Get all the produitPartenaires.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ProduitPartenaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProduitPartenaires");
        Page<ProduitPartenaire> result = produitPartenaireRepository.findAll(pageable);
        return result.map(produitPartenaire -> produitPartenaireMapper.produitPartenaireToProduitPartenaireDTO(produitPartenaire));
    }

    /**
     *  Get one produitPartenaire by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProduitPartenaireDTO findOne(Long id) {
        log.debug("Request to get ProduitPartenaire : {}", id);
        ProduitPartenaire produitPartenaire = produitPartenaireRepository.findOne(id);
        ProduitPartenaireDTO produitPartenaireDTO = produitPartenaireMapper.produitPartenaireToProduitPartenaireDTO(produitPartenaire);
        return produitPartenaireDTO;
    }

    /**
     *  Delete the  produitPartenaire by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProduitPartenaire : {}", id);
        produitPartenaireRepository.delete(id);
        produitPartenaireSearchRepository.delete(id);
    }

    /**
     * Search for the produitPartenaire corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProduitPartenaireDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProduitPartenaires for query {}", query);
        Page<ProduitPartenaire> result = produitPartenaireSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(produitPartenaire -> produitPartenaireMapper.produitPartenaireToProduitPartenaireDTO(produitPartenaire));
    }
}
