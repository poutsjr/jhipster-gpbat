package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.Partenaire;
import fr.kearis.gpbat.admin.repository.PartenaireRepository;
import fr.kearis.gpbat.admin.repository.search.PartenaireSearchRepository;
import fr.kearis.gpbat.admin.service.dto.PartenaireDTO;
import fr.kearis.gpbat.admin.service.mapper.PartenaireMapper;
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
 * Service Implementation for managing Partenaire.
 */
@Service
@Transactional
public class PartenaireService {

    private final Logger log = LoggerFactory.getLogger(PartenaireService.class);
    
    @Inject
    private PartenaireRepository partenaireRepository;

    @Inject
    private PartenaireMapper partenaireMapper;

    @Inject
    private PartenaireSearchRepository partenaireSearchRepository;

    /**
     * Save a partenaire.
     *
     * @param partenaireDTO the entity to save
     * @return the persisted entity
     */
    public PartenaireDTO save(PartenaireDTO partenaireDTO) {
        log.debug("Request to save Partenaire : {}", partenaireDTO);
        Partenaire partenaire = partenaireMapper.partenaireDTOToPartenaire(partenaireDTO);
        partenaire = partenaireRepository.save(partenaire);
        PartenaireDTO result = partenaireMapper.partenaireToPartenaireDTO(partenaire);
        partenaireSearchRepository.save(partenaire);
        return result;
    }

    /**
     *  Get all the partenaires.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PartenaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Partenaires");
        Page<Partenaire> result = partenaireRepository.findAll(pageable);
        return result.map(partenaire -> partenaireMapper.partenaireToPartenaireDTO(partenaire));
    }

    /**
     *  Get one partenaire by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PartenaireDTO findOne(Long id) {
        log.debug("Request to get Partenaire : {}", id);
        Partenaire partenaire = partenaireRepository.findOne(id);
        PartenaireDTO partenaireDTO = partenaireMapper.partenaireToPartenaireDTO(partenaire);
        return partenaireDTO;
    }

    /**
     *  Delete the  partenaire by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Partenaire : {}", id);
        partenaireRepository.delete(id);
        partenaireSearchRepository.delete(id);
    }

    /**
     * Search for the partenaire corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PartenaireDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Partenaires for query {}", query);
        Page<Partenaire> result = partenaireSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(partenaire -> partenaireMapper.partenaireToPartenaireDTO(partenaire));
    }
}
