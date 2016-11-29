package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.BordereauService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.BordereauDTO;
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
 * REST controller for managing Bordereau.
 */
@RestController
@RequestMapping("/api")
public class BordereauResource {

    private final Logger log = LoggerFactory.getLogger(BordereauResource.class);
        
    @Inject
    private BordereauService bordereauService;

    /**
     * POST  /bordereaus : Create a new bordereau.
     *
     * @param bordereauDTO the bordereauDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bordereauDTO, or with status 400 (Bad Request) if the bordereau has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bordereaus")
    @Timed
    public ResponseEntity<BordereauDTO> createBordereau(@RequestBody BordereauDTO bordereauDTO) throws URISyntaxException {
        log.debug("REST request to save Bordereau : {}", bordereauDTO);
        if (bordereauDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bordereau", "idexists", "A new bordereau cannot already have an ID")).body(null);
        }
        BordereauDTO result = bordereauService.save(bordereauDTO);
        return ResponseEntity.created(new URI("/api/bordereaus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bordereau", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bordereaus : Updates an existing bordereau.
     *
     * @param bordereauDTO the bordereauDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bordereauDTO,
     * or with status 400 (Bad Request) if the bordereauDTO is not valid,
     * or with status 500 (Internal Server Error) if the bordereauDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bordereaus")
    @Timed
    public ResponseEntity<BordereauDTO> updateBordereau(@RequestBody BordereauDTO bordereauDTO) throws URISyntaxException {
        log.debug("REST request to update Bordereau : {}", bordereauDTO);
        if (bordereauDTO.getId() == null) {
            return createBordereau(bordereauDTO);
        }
        BordereauDTO result = bordereauService.save(bordereauDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bordereau", bordereauDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bordereaus : get all the bordereaus.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bordereaus in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bordereaus")
    @Timed
    public ResponseEntity<List<BordereauDTO>> getAllBordereaus(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bordereaus");
        Page<BordereauDTO> page = bordereauService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bordereaus");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bordereaus/:id : get the "id" bordereau.
     *
     * @param id the id of the bordereauDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bordereauDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bordereaus/{id}")
    @Timed
    public ResponseEntity<BordereauDTO> getBordereau(@PathVariable Long id) {
        log.debug("REST request to get Bordereau : {}", id);
        BordereauDTO bordereauDTO = bordereauService.findOne(id);
        return Optional.ofNullable(bordereauDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bordereaus/:id : delete the "id" bordereau.
     *
     * @param id the id of the bordereauDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bordereaus/{id}")
    @Timed
    public ResponseEntity<Void> deleteBordereau(@PathVariable Long id) {
        log.debug("REST request to delete Bordereau : {}", id);
        bordereauService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bordereau", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bordereaus?query=:query : search for the bordereau corresponding
     * to the query.
     *
     * @param query the query of the bordereau search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/bordereaus")
    @Timed
    public ResponseEntity<List<BordereauDTO>> searchBordereaus(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Bordereaus for query {}", query);
        Page<BordereauDTO> page = bordereauService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bordereaus");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
