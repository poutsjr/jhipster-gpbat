package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.ReserveChantier;
import fr.kearis.gpbat.admin.repository.ReserveChantierRepository;
import fr.kearis.gpbat.admin.repository.search.ReserveChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ReserveChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.ReserveChantierMapper;
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
 * Service Implementation for managing ReserveChantier.
 */
@Service
@Transactional
public class ReserveChantierService {

    private final Logger log = LoggerFactory.getLogger(ReserveChantierService.class);
    
    @Inject
    private ReserveChantierRepository reserveChantierRepository;

    @Inject
    private ReserveChantierMapper reserveChantierMapper;

    @Inject
    private ReserveChantierSearchRepository reserveChantierSearchRepository;

    /**
     * Save a reserveChantier.
     *
     * @param reserveChantierDTO the entity to save
     * @return the persisted entity
     */
    public ReserveChantierDTO save(ReserveChantierDTO reserveChantierDTO) {
        log.debug("Request to save ReserveChantier : {}", reserveChantierDTO);
        ReserveChantier reserveChantier = reserveChantierMapper.reserveChantierDTOToReserveChantier(reserveChantierDTO);
        reserveChantier = reserveChantierRepository.save(reserveChantier);
        ReserveChantierDTO result = reserveChantierMapper.reserveChantierToReserveChantierDTO(reserveChantier);
        reserveChantierSearchRepository.save(reserveChantier);
        return result;
    }

    /**
     *  Get all the reserveChantiers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ReserveChantierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReserveChantiers");
        Page<ReserveChantier> result = reserveChantierRepository.findAll(pageable);
        return result.map(reserveChantier -> reserveChantierMapper.reserveChantierToReserveChantierDTO(reserveChantier));
    }

    /**
     *  Get one reserveChantier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ReserveChantierDTO findOne(Long id) {
        log.debug("Request to get ReserveChantier : {}", id);
        ReserveChantier reserveChantier = reserveChantierRepository.findOne(id);
        ReserveChantierDTO reserveChantierDTO = reserveChantierMapper.reserveChantierToReserveChantierDTO(reserveChantier);
        return reserveChantierDTO;
    }

    /**
     *  Delete the  reserveChantier by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReserveChantier : {}", id);
        reserveChantierRepository.delete(id);
        reserveChantierSearchRepository.delete(id);
    }

    /**
     * Search for the reserveChantier corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReserveChantierDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReserveChantiers for query {}", query);
        Page<ReserveChantier> result = reserveChantierSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(reserveChantier -> reserveChantierMapper.reserveChantierToReserveChantierDTO(reserveChantier));
    }
}
