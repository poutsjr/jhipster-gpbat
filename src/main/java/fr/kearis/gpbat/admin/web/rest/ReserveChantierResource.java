package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.ReserveChantierService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.ReserveChantierDTO;
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
 * REST controller for managing ReserveChantier.
 */
@RestController
@RequestMapping("/api")
public class ReserveChantierResource {

    private final Logger log = LoggerFactory.getLogger(ReserveChantierResource.class);
        
    @Inject
    private ReserveChantierService reserveChantierService;

    /**
     * POST  /reserve-chantiers : Create a new reserveChantier.
     *
     * @param reserveChantierDTO the reserveChantierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reserveChantierDTO, or with status 400 (Bad Request) if the reserveChantier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reserve-chantiers")
    @Timed
    public ResponseEntity<ReserveChantierDTO> createReserveChantier(@RequestBody ReserveChantierDTO reserveChantierDTO) throws URISyntaxException {
        log.debug("REST request to save ReserveChantier : {}", reserveChantierDTO);
        if (reserveChantierDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("reserveChantier", "idexists", "A new reserveChantier cannot already have an ID")).body(null);
        }
        ReserveChantierDTO result = reserveChantierService.save(reserveChantierDTO);
        return ResponseEntity.created(new URI("/api/reserve-chantiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reserveChantier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reserve-chantiers : Updates an existing reserveChantier.
     *
     * @param reserveChantierDTO the reserveChantierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reserveChantierDTO,
     * or with status 400 (Bad Request) if the reserveChantierDTO is not valid,
     * or with status 500 (Internal Server Error) if the reserveChantierDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reserve-chantiers")
    @Timed
    public ResponseEntity<ReserveChantierDTO> updateReserveChantier(@RequestBody ReserveChantierDTO reserveChantierDTO) throws URISyntaxException {
        log.debug("REST request to update ReserveChantier : {}", reserveChantierDTO);
        if (reserveChantierDTO.getId() == null) {
            return createReserveChantier(reserveChantierDTO);
        }
        ReserveChantierDTO result = reserveChantierService.save(reserveChantierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reserveChantier", reserveChantierDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reserve-chantiers : get all the reserveChantiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reserveChantiers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/reserve-chantiers")
    @Timed
    public ResponseEntity<List<ReserveChantierDTO>> getAllReserveChantiers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ReserveChantiers");
        Page<ReserveChantierDTO> page = reserveChantierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reserve-chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reserve-chantiers/:id : get the "id" reserveChantier.
     *
     * @param id the id of the reserveChantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reserveChantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reserve-chantiers/{id}")
    @Timed
    public ResponseEntity<ReserveChantierDTO> getReserveChantier(@PathVariable Long id) {
        log.debug("REST request to get ReserveChantier : {}", id);
        ReserveChantierDTO reserveChantierDTO = reserveChantierService.findOne(id);
        return Optional.ofNullable(reserveChantierDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reserve-chantiers/:id : delete the "id" reserveChantier.
     *
     * @param id the id of the reserveChantierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reserve-chantiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteReserveChantier(@PathVariable Long id) {
        log.debug("REST request to delete ReserveChantier : {}", id);
        reserveChantierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reserveChantier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/reserve-chantiers?query=:query : search for the reserveChantier corresponding
     * to the query.
     *
     * @param query the query of the reserveChantier search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/reserve-chantiers")
    @Timed
    public ResponseEntity<List<ReserveChantierDTO>> searchReserveChantiers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ReserveChantiers for query {}", query);
        Page<ReserveChantierDTO> page = reserveChantierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reserve-chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
