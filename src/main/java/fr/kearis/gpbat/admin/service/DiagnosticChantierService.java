package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.DiagnosticChantier;
import fr.kearis.gpbat.admin.repository.DiagnosticChantierRepository;
import fr.kearis.gpbat.admin.repository.search.DiagnosticChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.DiagnosticChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.DiagnosticChantierMapper;
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
 * Service Implementation for managing DiagnosticChantier.
 */
@Service
@Transactional
public class DiagnosticChantierService {

    private final Logger log = LoggerFactory.getLogger(DiagnosticChantierService.class);
    
    @Inject
    private DiagnosticChantierRepository diagnosticChantierRepository;

    @Inject
    private DiagnosticChantierMapper diagnosticChantierMapper;

    @Inject
    private DiagnosticChantierSearchRepository diagnosticChantierSearchRepository;

    /**
     * Save a diagnosticChantier.
     *
     * @param diagnosticChantierDTO the entity to save
     * @return the persisted entity
     */
    public DiagnosticChantierDTO save(DiagnosticChantierDTO diagnosticChantierDTO) {
        log.debug("Request to save DiagnosticChantier : {}", diagnosticChantierDTO);
        DiagnosticChantier diagnosticChantier = diagnosticChantierMapper.diagnosticChantierDTOToDiagnosticChantier(diagnosticChantierDTO);
        diagnosticChantier = diagnosticChantierRepository.save(diagnosticChantier);
        DiagnosticChantierDTO result = diagnosticChantierMapper.diagnosticChantierToDiagnosticChantierDTO(diagnosticChantier);
        diagnosticChantierSearchRepository.save(diagnosticChantier);
        return result;
    }

    /**
     *  Get all the diagnosticChantiers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DiagnosticChantierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DiagnosticChantiers");
        Page<DiagnosticChantier> result = diagnosticChantierRepository.findAll(pageable);
        return result.map(diagnosticChantier -> diagnosticChantierMapper.diagnosticChantierToDiagnosticChantierDTO(diagnosticChantier));
    }

    /**
     *  Get one diagnosticChantier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DiagnosticChantierDTO findOne(Long id) {
        log.debug("Request to get DiagnosticChantier : {}", id);
        DiagnosticChantier diagnosticChantier = diagnosticChantierRepository.findOne(id);
        DiagnosticChantierDTO diagnosticChantierDTO = diagnosticChantierMapper.diagnosticChantierToDiagnosticChantierDTO(diagnosticChantier);
        return diagnosticChantierDTO;
    }

    /**
     *  Delete the  diagnosticChantier by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DiagnosticChantier : {}", id);
        diagnosticChantierRepository.delete(id);
        diagnosticChantierSearchRepository.delete(id);
    }

    /**
     * Search for the diagnosticChantier corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DiagnosticChantierDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DiagnosticChantiers for query {}", query);
        Page<DiagnosticChantier> result = diagnosticChantierSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(diagnosticChantier -> diagnosticChantierMapper.diagnosticChantierToDiagnosticChantierDTO(diagnosticChantier));
    }
}
