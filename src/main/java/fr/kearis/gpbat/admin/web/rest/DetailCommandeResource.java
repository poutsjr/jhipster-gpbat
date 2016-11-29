package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.DetailCommandeService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.DetailCommandeDTO;
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
 * REST controller for managing DetailCommande.
 */
@RestController
@RequestMapping("/api")
public class DetailCommandeResource {

    private final Logger log = LoggerFactory.getLogger(DetailCommandeResource.class);
        
    @Inject
    private DetailCommandeService detailCommandeService;

    /**
     * POST  /detail-commandes : Create a new detailCommande.
     *
     * @param detailCommandeDTO the detailCommandeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new detailCommandeDTO, or with status 400 (Bad Request) if the detailCommande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/detail-commandes")
    @Timed
    public ResponseEntity<DetailCommandeDTO> createDetailCommande(@RequestBody DetailCommandeDTO detailCommandeDTO) throws URISyntaxException {
        log.debug("REST request to save DetailCommande : {}", detailCommandeDTO);
        if (detailCommandeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("detailCommande", "idexists", "A new detailCommande cannot already have an ID")).body(null);
        }
        DetailCommandeDTO result = detailCommandeService.save(detailCommandeDTO);
        return ResponseEntity.created(new URI("/api/detail-commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("detailCommande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /detail-commandes : Updates an existing detailCommande.
     *
     * @param detailCommandeDTO the detailCommandeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated detailCommandeDTO,
     * or with status 400 (Bad Request) if the detailCommandeDTO is not valid,
     * or with status 500 (Internal Server Error) if the detailCommandeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/detail-commandes")
    @Timed
    public ResponseEntity<DetailCommandeDTO> updateDetailCommande(@RequestBody DetailCommandeDTO detailCommandeDTO) throws URISyntaxException {
        log.debug("REST request to update DetailCommande : {}", detailCommandeDTO);
        if (detailCommandeDTO.getId() == null) {
            return createDetailCommande(detailCommandeDTO);
        }
        DetailCommandeDTO result = detailCommandeService.save(detailCommandeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("detailCommande", detailCommandeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /detail-commandes : get all the detailCommandes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of detailCommandes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/detail-commandes")
    @Timed
    public ResponseEntity<List<DetailCommandeDTO>> getAllDetailCommandes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DetailCommandes");
        Page<DetailCommandeDTO> page = detailCommandeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/detail-commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /detail-commandes/:id : get the "id" detailCommande.
     *
     * @param id the id of the detailCommandeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the detailCommandeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/detail-commandes/{id}")
    @Timed
    public ResponseEntity<DetailCommandeDTO> getDetailCommande(@PathVariable Long id) {
        log.debug("REST request to get DetailCommande : {}", id);
        DetailCommandeDTO detailCommandeDTO = detailCommandeService.findOne(id);
        return Optional.ofNullable(detailCommandeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /detail-commandes/:id : delete the "id" detailCommande.
     *
     * @param id the id of the detailCommandeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/detail-commandes/{id}")
    @Timed
    public ResponseEntity<Void> deleteDetailCommande(@PathVariable Long id) {
        log.debug("REST request to delete DetailCommande : {}", id);
        detailCommandeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("detailCommande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/detail-commandes?query=:query : search for the detailCommande corresponding
     * to the query.
     *
     * @param query the query of the detailCommande search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/detail-commandes")
    @Timed
    public ResponseEntity<List<DetailCommandeDTO>> searchDetailCommandes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DetailCommandes for query {}", query);
        Page<DetailCommandeDTO> page = detailCommandeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/detail-commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
