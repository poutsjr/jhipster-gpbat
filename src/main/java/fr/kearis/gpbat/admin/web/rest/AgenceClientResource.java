package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.AgenceClientService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.AgenceClientDTO;
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
 * REST controller for managing AgenceClient.
 */
@RestController
@RequestMapping("/api")
public class AgenceClientResource {

    private final Logger log = LoggerFactory.getLogger(AgenceClientResource.class);
        
    @Inject
    private AgenceClientService agenceClientService;

    /**
     * POST  /agence-clients : Create a new agenceClient.
     *
     * @param agenceClientDTO the agenceClientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agenceClientDTO, or with status 400 (Bad Request) if the agenceClient has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agence-clients")
    @Timed
    public ResponseEntity<AgenceClientDTO> createAgenceClient(@RequestBody AgenceClientDTO agenceClientDTO) throws URISyntaxException {
        log.debug("REST request to save AgenceClient : {}", agenceClientDTO);
        if (agenceClientDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("agenceClient", "idexists", "A new agenceClient cannot already have an ID")).body(null);
        }
        AgenceClientDTO result = agenceClientService.save(agenceClientDTO);
        return ResponseEntity.created(new URI("/api/agence-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("agenceClient", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agence-clients : Updates an existing agenceClient.
     *
     * @param agenceClientDTO the agenceClientDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agenceClientDTO,
     * or with status 400 (Bad Request) if the agenceClientDTO is not valid,
     * or with status 500 (Internal Server Error) if the agenceClientDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agence-clients")
    @Timed
    public ResponseEntity<AgenceClientDTO> updateAgenceClient(@RequestBody AgenceClientDTO agenceClientDTO) throws URISyntaxException {
        log.debug("REST request to update AgenceClient : {}", agenceClientDTO);
        if (agenceClientDTO.getId() == null) {
            return createAgenceClient(agenceClientDTO);
        }
        AgenceClientDTO result = agenceClientService.save(agenceClientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("agenceClient", agenceClientDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agence-clients : get all the agenceClients.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agenceClients in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/agence-clients")
    @Timed
    public ResponseEntity<List<AgenceClientDTO>> getAllAgenceClients(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AgenceClients");
        Page<AgenceClientDTO> page = agenceClientService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agence-clients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agence-clients/:id : get the "id" agenceClient.
     *
     * @param id the id of the agenceClientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agenceClientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/agence-clients/{id}")
    @Timed
    public ResponseEntity<AgenceClientDTO> getAgenceClient(@PathVariable Long id) {
        log.debug("REST request to get AgenceClient : {}", id);
        AgenceClientDTO agenceClientDTO = agenceClientService.findOne(id);
        return Optional.ofNullable(agenceClientDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /agence-clients/:id : delete the "id" agenceClient.
     *
     * @param id the id of the agenceClientDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agence-clients/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgenceClient(@PathVariable Long id) {
        log.debug("REST request to delete AgenceClient : {}", id);
        agenceClientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("agenceClient", id.toString())).build();
    }

    /**
     * SEARCH  /_search/agence-clients?query=:query : search for the agenceClient corresponding
     * to the query.
     *
     * @param query the query of the agenceClient search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/agence-clients")
    @Timed
    public ResponseEntity<List<AgenceClientDTO>> searchAgenceClients(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AgenceClients for query {}", query);
        Page<AgenceClientDTO> page = agenceClientService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/agence-clients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
