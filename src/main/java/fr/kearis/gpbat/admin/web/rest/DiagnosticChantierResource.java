package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.DiagnosticChantierService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.DiagnosticChantierDTO;
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
 * REST controller for managing DiagnosticChantier.
 */
@RestController
@RequestMapping("/api")
public class DiagnosticChantierResource {

    private final Logger log = LoggerFactory.getLogger(DiagnosticChantierResource.class);
        
    @Inject
    private DiagnosticChantierService diagnosticChantierService;

    /**
     * POST  /diagnostic-chantiers : Create a new diagnosticChantier.
     *
     * @param diagnosticChantierDTO the diagnosticChantierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diagnosticChantierDTO, or with status 400 (Bad Request) if the diagnosticChantier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/diagnostic-chantiers")
    @Timed
    public ResponseEntity<DiagnosticChantierDTO> createDiagnosticChantier(@RequestBody DiagnosticChantierDTO diagnosticChantierDTO) throws URISyntaxException {
        log.debug("REST request to save DiagnosticChantier : {}", diagnosticChantierDTO);
        if (diagnosticChantierDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("diagnosticChantier", "idexists", "A new diagnosticChantier cannot already have an ID")).body(null);
        }
        DiagnosticChantierDTO result = diagnosticChantierService.save(diagnosticChantierDTO);
        return ResponseEntity.created(new URI("/api/diagnostic-chantiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("diagnosticChantier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diagnostic-chantiers : Updates an existing diagnosticChantier.
     *
     * @param diagnosticChantierDTO the diagnosticChantierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diagnosticChantierDTO,
     * or with status 400 (Bad Request) if the diagnosticChantierDTO is not valid,
     * or with status 500 (Internal Server Error) if the diagnosticChantierDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/diagnostic-chantiers")
    @Timed
    public ResponseEntity<DiagnosticChantierDTO> updateDiagnosticChantier(@RequestBody DiagnosticChantierDTO diagnosticChantierDTO) throws URISyntaxException {
        log.debug("REST request to update DiagnosticChantier : {}", diagnosticChantierDTO);
        if (diagnosticChantierDTO.getId() == null) {
            return createDiagnosticChantier(diagnosticChantierDTO);
        }
        DiagnosticChantierDTO result = diagnosticChantierService.save(diagnosticChantierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("diagnosticChantier", diagnosticChantierDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /diagnostic-chantiers : get all the diagnosticChantiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of diagnosticChantiers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/diagnostic-chantiers")
    @Timed
    public ResponseEntity<List<DiagnosticChantierDTO>> getAllDiagnosticChantiers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DiagnosticChantiers");
        Page<DiagnosticChantierDTO> page = diagnosticChantierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/diagnostic-chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /diagnostic-chantiers/:id : get the "id" diagnosticChantier.
     *
     * @param id the id of the diagnosticChantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diagnosticChantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/diagnostic-chantiers/{id}")
    @Timed
    public ResponseEntity<DiagnosticChantierDTO> getDiagnosticChantier(@PathVariable Long id) {
        log.debug("REST request to get DiagnosticChantier : {}", id);
        DiagnosticChantierDTO diagnosticChantierDTO = diagnosticChantierService.findOne(id);
        return Optional.ofNullable(diagnosticChantierDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /diagnostic-chantiers/:id : delete the "id" diagnosticChantier.
     *
     * @param id the id of the diagnosticChantierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/diagnostic-chantiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteDiagnosticChantier(@PathVariable Long id) {
        log.debug("REST request to delete DiagnosticChantier : {}", id);
        diagnosticChantierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("diagnosticChantier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/diagnostic-chantiers?query=:query : search for the diagnosticChantier corresponding
     * to the query.
     *
     * @param query the query of the diagnosticChantier search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/diagnostic-chantiers")
    @Timed
    public ResponseEntity<List<DiagnosticChantierDTO>> searchDiagnosticChantiers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DiagnosticChantiers for query {}", query);
        Page<DiagnosticChantierDTO> page = diagnosticChantierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/diagnostic-chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
