package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.Bordereau;
import fr.kearis.gpbat.admin.repository.BordereauRepository;
import fr.kearis.gpbat.admin.repository.search.BordereauSearchRepository;
import fr.kearis.gpbat.admin.service.dto.BordereauDTO;
import fr.kearis.gpbat.admin.service.mapper.BordereauMapper;
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
 * Service Implementation for managing Bordereau.
 */
@Service
@Transactional
public class BordereauService {

    private final Logger log = LoggerFactory.getLogger(BordereauService.class);
    
    @Inject
    private BordereauRepository bordereauRepository;

    @Inject
    private BordereauMapper bordereauMapper;

    @Inject
    private BordereauSearchRepository bordereauSearchRepository;

    /**
     * Save a bordereau.
     *
     * @param bordereauDTO the entity to save
     * @return the persisted entity
     */
    public BordereauDTO save(BordereauDTO bordereauDTO) {
        log.debug("Request to save Bordereau : {}", bordereauDTO);
        Bordereau bordereau = bordereauMapper.bordereauDTOToBordereau(bordereauDTO);
        bordereau = bordereauRepository.save(bordereau);
        BordereauDTO result = bordereauMapper.bordereauToBordereauDTO(bordereau);
        bordereauSearchRepository.save(bordereau);
        return result;
    }

    /**
     *  Get all the bordereaus.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BordereauDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bordereaus");
        Page<Bordereau> result = bordereauRepository.findAll(pageable);
        return result.map(bordereau -> bordereauMapper.bordereauToBordereauDTO(bordereau));
    }

    /**
     *  Get one bordereau by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BordereauDTO findOne(Long id) {
        log.debug("Request to get Bordereau : {}", id);
        Bordereau bordereau = bordereauRepository.findOne(id);
        BordereauDTO bordereauDTO = bordereauMapper.bordereauToBordereauDTO(bordereau);
        return bordereauDTO;
    }

    /**
     *  Delete the  bordereau by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bordereau : {}", id);
        bordereauRepository.delete(id);
        bordereauSearchRepository.delete(id);
    }

    /**
     * Search for the bordereau corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BordereauDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bordereaus for query {}", query);
        Page<Bordereau> result = bordereauSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(bordereau -> bordereauMapper.bordereauToBordereauDTO(bordereau));
    }
}
