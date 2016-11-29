package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.PartenaireService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.PartenaireDTO;
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
 * REST controller for managing Partenaire.
 */
@RestController
@RequestMapping("/api")
public class PartenaireResource {

    private final Logger log = LoggerFactory.getLogger(PartenaireResource.class);
        
    @Inject
    private PartenaireService partenaireService;

    /**
     * POST  /partenaires : Create a new partenaire.
     *
     * @param partenaireDTO the partenaireDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new partenaireDTO, or with status 400 (Bad Request) if the partenaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/partenaires")
    @Timed
    public ResponseEntity<PartenaireDTO> createPartenaire(@RequestBody PartenaireDTO partenaireDTO) throws URISyntaxException {
        log.debug("REST request to save Partenaire : {}", partenaireDTO);
        if (partenaireDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("partenaire", "idexists", "A new partenaire cannot already have an ID")).body(null);
        }
        PartenaireDTO result = partenaireService.save(partenaireDTO);
        return ResponseEntity.created(new URI("/api/partenaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("partenaire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /partenaires : Updates an existing partenaire.
     *
     * @param partenaireDTO the partenaireDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated partenaireDTO,
     * or with status 400 (Bad Request) if the partenaireDTO is not valid,
     * or with status 500 (Internal Server Error) if the partenaireDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/partenaires")
    @Timed
    public ResponseEntity<PartenaireDTO> updatePartenaire(@RequestBody PartenaireDTO partenaireDTO) throws URISyntaxException {
        log.debug("REST request to update Partenaire : {}", partenaireDTO);
        if (partenaireDTO.getId() == null) {
            return createPartenaire(partenaireDTO);
        }
        PartenaireDTO result = partenaireService.save(partenaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("partenaire", partenaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /partenaires : get all the partenaires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of partenaires in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/partenaires")
    @Timed
    public ResponseEntity<List<PartenaireDTO>> getAllPartenaires(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Partenaires");
        Page<PartenaireDTO> page = partenaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/partenaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /partenaires/:id : get the "id" partenaire.
     *
     * @param id the id of the partenaireDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the partenaireDTO, or with status 404 (Not Found)
     */
    @GetMapping("/partenaires/{id}")
    @Timed
    public ResponseEntity<PartenaireDTO> getPartenaire(@PathVariable Long id) {
        log.debug("REST request to get Partenaire : {}", id);
        PartenaireDTO partenaireDTO = partenaireService.findOne(id);
        return Optional.ofNullable(partenaireDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /partenaires/:id : delete the "id" partenaire.
     *
     * @param id the id of the partenaireDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/partenaires/{id}")
    @Timed
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        log.debug("REST request to delete Partenaire : {}", id);
        partenaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("partenaire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/partenaires?query=:query : search for the partenaire corresponding
     * to the query.
     *
     * @param query the query of the partenaire search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/partenaires")
    @Timed
    public ResponseEntity<List<PartenaireDTO>> searchPartenaires(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Partenaires for query {}", query);
        Page<PartenaireDTO> page = partenaireService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/partenaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
