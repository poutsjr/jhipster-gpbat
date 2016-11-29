package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.ChantierService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.ChantierDTO;
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
 * REST controller for managing Chantier.
 */
@RestController
@RequestMapping("/api")
public class ChantierResource {

    private final Logger log = LoggerFactory.getLogger(ChantierResource.class);
        
    @Inject
    private ChantierService chantierService;

    /**
     * POST  /chantiers : Create a new chantier.
     *
     * @param chantierDTO the chantierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chantierDTO, or with status 400 (Bad Request) if the chantier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chantiers")
    @Timed
    public ResponseEntity<ChantierDTO> createChantier(@RequestBody ChantierDTO chantierDTO) throws URISyntaxException {
        log.debug("REST request to save Chantier : {}", chantierDTO);
        if (chantierDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("chantier", "idexists", "A new chantier cannot already have an ID")).body(null);
        }
        ChantierDTO result = chantierService.save(chantierDTO);
        return ResponseEntity.created(new URI("/api/chantiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("chantier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chantiers : Updates an existing chantier.
     *
     * @param chantierDTO the chantierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chantierDTO,
     * or with status 400 (Bad Request) if the chantierDTO is not valid,
     * or with status 500 (Internal Server Error) if the chantierDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chantiers")
    @Timed
    public ResponseEntity<ChantierDTO> updateChantier(@RequestBody ChantierDTO chantierDTO) throws URISyntaxException {
        log.debug("REST request to update Chantier : {}", chantierDTO);
        if (chantierDTO.getId() == null) {
            return createChantier(chantierDTO);
        }
        ChantierDTO result = chantierService.save(chantierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("chantier", chantierDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chantiers : get all the chantiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of chantiers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/chantiers")
    @Timed
    public ResponseEntity<List<ChantierDTO>> getAllChantiers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Chantiers");
        Page<ChantierDTO> page = chantierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chantiers/{id}")
    @Timed
    public ResponseEntity<ChantierDTO> getChantier(@PathVariable Long id) {
        log.debug("REST request to get Chantier : {}", id);
        ChantierDTO chantierDTO = chantierService.findOne(id);
        return Optional.ofNullable(chantierDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /chantiers/:id : delete the "id" chantier.
     *
     * @param id the id of the chantierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chantiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteChantier(@PathVariable Long id) {
        log.debug("REST request to delete Chantier : {}", id);
        chantierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("chantier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/chantiers?query=:query : search for the chantier corresponding
     * to the query.
     *
     * @param query the query of the chantier search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/chantiers")
    @Timed
    public ResponseEntity<List<ChantierDTO>> searchChantiers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Chantiers for query {}", query);
        Page<ChantierDTO> page = chantierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
