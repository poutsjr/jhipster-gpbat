package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.ReceptionChantierService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.ReceptionChantierDTO;
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
 * REST controller for managing ReceptionChantier.
 */
@RestController
@RequestMapping("/api")
public class ReceptionChantierResource {

    private final Logger log = LoggerFactory.getLogger(ReceptionChantierResource.class);
        
    @Inject
    private ReceptionChantierService receptionChantierService;

    /**
     * POST  /reception-chantiers : Create a new receptionChantier.
     *
     * @param receptionChantierDTO the receptionChantierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new receptionChantierDTO, or with status 400 (Bad Request) if the receptionChantier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reception-chantiers")
    @Timed
    public ResponseEntity<ReceptionChantierDTO> createReceptionChantier(@RequestBody ReceptionChantierDTO receptionChantierDTO) throws URISyntaxException {
        log.debug("REST request to save ReceptionChantier : {}", receptionChantierDTO);
        if (receptionChantierDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("receptionChantier", "idexists", "A new receptionChantier cannot already have an ID")).body(null);
        }
        ReceptionChantierDTO result = receptionChantierService.save(receptionChantierDTO);
        return ResponseEntity.created(new URI("/api/reception-chantiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("receptionChantier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reception-chantiers : Updates an existing receptionChantier.
     *
     * @param receptionChantierDTO the receptionChantierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated receptionChantierDTO,
     * or with status 400 (Bad Request) if the receptionChantierDTO is not valid,
     * or with status 500 (Internal Server Error) if the receptionChantierDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reception-chantiers")
    @Timed
    public ResponseEntity<ReceptionChantierDTO> updateReceptionChantier(@RequestBody ReceptionChantierDTO receptionChantierDTO) throws URISyntaxException {
        log.debug("REST request to update ReceptionChantier : {}", receptionChantierDTO);
        if (receptionChantierDTO.getId() == null) {
            return createReceptionChantier(receptionChantierDTO);
        }
        ReceptionChantierDTO result = receptionChantierService.save(receptionChantierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("receptionChantier", receptionChantierDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reception-chantiers : get all the receptionChantiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of receptionChantiers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/reception-chantiers")
    @Timed
    public ResponseEntity<List<ReceptionChantierDTO>> getAllReceptionChantiers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ReceptionChantiers");
        Page<ReceptionChantierDTO> page = receptionChantierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reception-chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reception-chantiers/:id : get the "id" receptionChantier.
     *
     * @param id the id of the receptionChantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the receptionChantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reception-chantiers/{id}")
    @Timed
    public ResponseEntity<ReceptionChantierDTO> getReceptionChantier(@PathVariable Long id) {
        log.debug("REST request to get ReceptionChantier : {}", id);
        ReceptionChantierDTO receptionChantierDTO = receptionChantierService.findOne(id);
        return Optional.ofNullable(receptionChantierDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reception-chantiers/:id : delete the "id" receptionChantier.
     *
     * @param id the id of the receptionChantierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reception-chantiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteReceptionChantier(@PathVariable Long id) {
        log.debug("REST request to delete ReceptionChantier : {}", id);
        receptionChantierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("receptionChantier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/reception-chantiers?query=:query : search for the receptionChantier corresponding
     * to the query.
     *
     * @param query the query of the receptionChantier search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/reception-chantiers")
    @Timed
    public ResponseEntity<List<ReceptionChantierDTO>> searchReceptionChantiers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ReceptionChantiers for query {}", query);
        Page<ReceptionChantierDTO> page = receptionChantierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reception-chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
