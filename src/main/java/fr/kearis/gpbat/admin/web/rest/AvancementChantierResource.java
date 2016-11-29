package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.AvancementChantierService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.AvancementChantierDTO;
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
 * REST controller for managing AvancementChantier.
 */
@RestController
@RequestMapping("/api")
public class AvancementChantierResource {

    private final Logger log = LoggerFactory.getLogger(AvancementChantierResource.class);
        
    @Inject
    private AvancementChantierService avancementChantierService;

    /**
     * POST  /avancement-chantiers : Create a new avancementChantier.
     *
     * @param avancementChantierDTO the avancementChantierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new avancementChantierDTO, or with status 400 (Bad Request) if the avancementChantier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/avancement-chantiers")
    @Timed
    public ResponseEntity<AvancementChantierDTO> createAvancementChantier(@RequestBody AvancementChantierDTO avancementChantierDTO) throws URISyntaxException {
        log.debug("REST request to save AvancementChantier : {}", avancementChantierDTO);
        if (avancementChantierDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("avancementChantier", "idexists", "A new avancementChantier cannot already have an ID")).body(null);
        }
        AvancementChantierDTO result = avancementChantierService.save(avancementChantierDTO);
        return ResponseEntity.created(new URI("/api/avancement-chantiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("avancementChantier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /avancement-chantiers : Updates an existing avancementChantier.
     *
     * @param avancementChantierDTO the avancementChantierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated avancementChantierDTO,
     * or with status 400 (Bad Request) if the avancementChantierDTO is not valid,
     * or with status 500 (Internal Server Error) if the avancementChantierDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/avancement-chantiers")
    @Timed
    public ResponseEntity<AvancementChantierDTO> updateAvancementChantier(@RequestBody AvancementChantierDTO avancementChantierDTO) throws URISyntaxException {
        log.debug("REST request to update AvancementChantier : {}", avancementChantierDTO);
        if (avancementChantierDTO.getId() == null) {
            return createAvancementChantier(avancementChantierDTO);
        }
        AvancementChantierDTO result = avancementChantierService.save(avancementChantierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("avancementChantier", avancementChantierDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /avancement-chantiers : get all the avancementChantiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of avancementChantiers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/avancement-chantiers")
    @Timed
    public ResponseEntity<List<AvancementChantierDTO>> getAllAvancementChantiers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AvancementChantiers");
        Page<AvancementChantierDTO> page = avancementChantierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/avancement-chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /avancement-chantiers/:id : get the "id" avancementChantier.
     *
     * @param id the id of the avancementChantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the avancementChantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/avancement-chantiers/{id}")
    @Timed
    public ResponseEntity<AvancementChantierDTO> getAvancementChantier(@PathVariable Long id) {
        log.debug("REST request to get AvancementChantier : {}", id);
        AvancementChantierDTO avancementChantierDTO = avancementChantierService.findOne(id);
        return Optional.ofNullable(avancementChantierDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /avancement-chantiers/:id : delete the "id" avancementChantier.
     *
     * @param id the id of the avancementChantierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/avancement-chantiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAvancementChantier(@PathVariable Long id) {
        log.debug("REST request to delete AvancementChantier : {}", id);
        avancementChantierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("avancementChantier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/avancement-chantiers?query=:query : search for the avancementChantier corresponding
     * to the query.
     *
     * @param query the query of the avancementChantier search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/avancement-chantiers")
    @Timed
    public ResponseEntity<List<AvancementChantierDTO>> searchAvancementChantiers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AvancementChantiers for query {}", query);
        Page<AvancementChantierDTO> page = avancementChantierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/avancement-chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
