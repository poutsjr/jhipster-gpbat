package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.CorpsEtatService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.CorpsEtatDTO;
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
 * REST controller for managing CorpsEtat.
 */
@RestController
@RequestMapping("/api")
public class CorpsEtatResource {

    private final Logger log = LoggerFactory.getLogger(CorpsEtatResource.class);
        
    @Inject
    private CorpsEtatService corpsEtatService;

    /**
     * POST  /corps-etats : Create a new corpsEtat.
     *
     * @param corpsEtatDTO the corpsEtatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new corpsEtatDTO, or with status 400 (Bad Request) if the corpsEtat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/corps-etats")
    @Timed
    public ResponseEntity<CorpsEtatDTO> createCorpsEtat(@RequestBody CorpsEtatDTO corpsEtatDTO) throws URISyntaxException {
        log.debug("REST request to save CorpsEtat : {}", corpsEtatDTO);
        if (corpsEtatDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("corpsEtat", "idexists", "A new corpsEtat cannot already have an ID")).body(null);
        }
        CorpsEtatDTO result = corpsEtatService.save(corpsEtatDTO);
        return ResponseEntity.created(new URI("/api/corps-etats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("corpsEtat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /corps-etats : Updates an existing corpsEtat.
     *
     * @param corpsEtatDTO the corpsEtatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated corpsEtatDTO,
     * or with status 400 (Bad Request) if the corpsEtatDTO is not valid,
     * or with status 500 (Internal Server Error) if the corpsEtatDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/corps-etats")
    @Timed
    public ResponseEntity<CorpsEtatDTO> updateCorpsEtat(@RequestBody CorpsEtatDTO corpsEtatDTO) throws URISyntaxException {
        log.debug("REST request to update CorpsEtat : {}", corpsEtatDTO);
        if (corpsEtatDTO.getId() == null) {
            return createCorpsEtat(corpsEtatDTO);
        }
        CorpsEtatDTO result = corpsEtatService.save(corpsEtatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("corpsEtat", corpsEtatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /corps-etats : get all the corpsEtats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of corpsEtats in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/corps-etats")
    @Timed
    public ResponseEntity<List<CorpsEtatDTO>> getAllCorpsEtats(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CorpsEtats");
        Page<CorpsEtatDTO> page = corpsEtatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/corps-etats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /corps-etats/:id : get the "id" corpsEtat.
     *
     * @param id the id of the corpsEtatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the corpsEtatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/corps-etats/{id}")
    @Timed
    public ResponseEntity<CorpsEtatDTO> getCorpsEtat(@PathVariable Long id) {
        log.debug("REST request to get CorpsEtat : {}", id);
        CorpsEtatDTO corpsEtatDTO = corpsEtatService.findOne(id);
        return Optional.ofNullable(corpsEtatDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /corps-etats/:id : delete the "id" corpsEtat.
     *
     * @param id the id of the corpsEtatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/corps-etats/{id}")
    @Timed
    public ResponseEntity<Void> deleteCorpsEtat(@PathVariable Long id) {
        log.debug("REST request to delete CorpsEtat : {}", id);
        corpsEtatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("corpsEtat", id.toString())).build();
    }

    /**
     * SEARCH  /_search/corps-etats?query=:query : search for the corpsEtat corresponding
     * to the query.
     *
     * @param query the query of the corpsEtat search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/corps-etats")
    @Timed
    public ResponseEntity<List<CorpsEtatDTO>> searchCorpsEtats(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CorpsEtats for query {}", query);
        Page<CorpsEtatDTO> page = corpsEtatService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/corps-etats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
