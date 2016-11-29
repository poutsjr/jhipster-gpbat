package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.Simulation;
import fr.kearis.gpbat.admin.repository.SimulationRepository;
import fr.kearis.gpbat.admin.repository.search.SimulationSearchRepository;
import fr.kearis.gpbat.admin.service.dto.SimulationDTO;
import fr.kearis.gpbat.admin.service.mapper.SimulationMapper;
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
 * Service Implementation for managing Simulation.
 */
@Service
@Transactional
public class SimulationService {

    private final Logger log = LoggerFactory.getLogger(SimulationService.class);
    
    @Inject
    private SimulationRepository simulationRepository;

    @Inject
    private SimulationMapper simulationMapper;

    @Inject
    private SimulationSearchRepository simulationSearchRepository;

    /**
     * Save a simulation.
     *
     * @param simulationDTO the entity to save
     * @return the persisted entity
     */
    public SimulationDTO save(SimulationDTO simulationDTO) {
        log.debug("Request to save Simulation : {}", simulationDTO);
        Simulation simulation = simulationMapper.simulationDTOToSimulation(simulationDTO);
        simulation = simulationRepository.save(simulation);
        SimulationDTO result = simulationMapper.simulationToSimulationDTO(simulation);
        simulationSearchRepository.save(simulation);
        return result;
    }

    /**
     *  Get all the simulations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SimulationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Simulations");
        Page<Simulation> result = simulationRepository.findAll(pageable);
        return result.map(simulation -> simulationMapper.simulationToSimulationDTO(simulation));
    }

    /**
     *  Get one simulation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SimulationDTO findOne(Long id) {
        log.debug("Request to get Simulation : {}", id);
        Simulation simulation = simulationRepository.findOne(id);
        SimulationDTO simulationDTO = simulationMapper.simulationToSimulationDTO(simulation);
        return simulationDTO;
    }

    /**
     *  Delete the  simulation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Simulation : {}", id);
        simulationRepository.delete(id);
        simulationSearchRepository.delete(id);
    }

    /**
     * Search for the simulation corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SimulationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Simulations for query {}", query);
        Page<Simulation> result = simulationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(simulation -> simulationMapper.simulationToSimulationDTO(simulation));
    }
}
