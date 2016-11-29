package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.SimulationService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.SimulationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Simulation.
 */
@RestController
@RequestMapping("/api")
public class SimulationResource {

    private final Logger log = LoggerFactory.getLogger(SimulationResource.class);
        
    @Inject
    private SimulationService simulationService;

    /**
     * POST  /simulations : Create a new simulation.
     *
     * @param simulationDTO the simulationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new simulationDTO, or with status 400 (Bad Request) if the simulation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/simulations")
    @Timed
    public ResponseEntity<SimulationDTO> createSimulation(@RequestBody SimulationDTO simulationDTO) throws URISyntaxException {
        log.debug("REST request to save Simulation : {}", simulationDTO);
        if (simulationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("simulation", "idexists", "A new simulation cannot already have an ID")).body(null);
        }
        SimulationDTO result = simulationService.save(simulationDTO);
        return ResponseEntity.created(new URI("/api/simulations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("simulation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /simulations : Updates an existing simulation.
     *
     * @param simulationDTO the simulationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated simulationDTO,
     * or with status 400 (Bad Request) if the simulationDTO is not valid,
     * or with status 500 (Internal Server Error) if the simulationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/simulations")
    @Timed
    public ResponseEntity<SimulationDTO> updateSimulation(@RequestBody SimulationDTO simulationDTO) throws URISyntaxException {
        log.debug("REST request to update Simulation : {}", simulationDTO);
        if (simulationDTO.getId() == null) {
            return createSimulation(simulationDTO);
        }
        SimulationDTO result = simulationService.save(simulationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("simulation", simulationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /simulations : get all the simulations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of simulations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/simulations")
    @Timed
    public ResponseEntity<List<SimulationDTO>> getAllSimulations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Simulations");
        Page<SimulationDTO> page = simulationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/simulations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /simulations/:id : get the "id" simulation.
     *
     * @param id the id of the simulationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the simulationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/simulations/{id}")
    @Timed
    public ResponseEntity<SimulationDTO> getSimulation(@PathVariable Long id) {
        log.debug("REST request to get Simulation : {}", id);
        SimulationDTO simulationDTO = simulationService.findOne(id);
        return Optional.ofNullable(simulationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /simulations/:id : delete the "id" simulation.
     *
     * @param id the id of the simulationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/simulations/{id}")
    @Timed
    public ResponseEntity<Void> deleteSimulation(@PathVariable Long id) {
        log.debug("REST request to delete Simulation : {}", id);
        simulationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("simulation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/simulations?query=:query : search for the simulation corresponding
     * to the query.
     *
     * @param query the query of the simulation search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/simulations")
    @Timed
    public ResponseEntity<List<SimulationDTO>> searchSimulations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Simulations for query {}", query);
        Page<SimulationDTO> page = simulationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/simulations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
