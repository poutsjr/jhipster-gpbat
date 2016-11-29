package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.TypeCommandeService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.TypeCommandeDTO;
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
 * REST controller for managing TypeCommande.
 */
@RestController
@RequestMapping("/api")
public class TypeCommandeResource {

    private final Logger log = LoggerFactory.getLogger(TypeCommandeResource.class);
        
    @Inject
    private TypeCommandeService typeCommandeService;

    /**
     * POST  /type-commandes : Create a new typeCommande.
     *
     * @param typeCommandeDTO the typeCommandeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeCommandeDTO, or with status 400 (Bad Request) if the typeCommande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-commandes")
    @Timed
    public ResponseEntity<TypeCommandeDTO> createTypeCommande(@RequestBody TypeCommandeDTO typeCommandeDTO) throws URISyntaxException {
        log.debug("REST request to save TypeCommande : {}", typeCommandeDTO);
        if (typeCommandeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeCommande", "idexists", "A new typeCommande cannot already have an ID")).body(null);
        }
        TypeCommandeDTO result = typeCommandeService.save(typeCommandeDTO);
        return ResponseEntity.created(new URI("/api/type-commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeCommande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-commandes : Updates an existing typeCommande.
     *
     * @param typeCommandeDTO the typeCommandeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeCommandeDTO,
     * or with status 400 (Bad Request) if the typeCommandeDTO is not valid,
     * or with status 500 (Internal Server Error) if the typeCommandeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-commandes")
    @Timed
    public ResponseEntity<TypeCommandeDTO> updateTypeCommande(@RequestBody TypeCommandeDTO typeCommandeDTO) throws URISyntaxException {
        log.debug("REST request to update TypeCommande : {}", typeCommandeDTO);
        if (typeCommandeDTO.getId() == null) {
            return createTypeCommande(typeCommandeDTO);
        }
        TypeCommandeDTO result = typeCommandeService.save(typeCommandeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeCommande", typeCommandeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-commandes : get all the typeCommandes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeCommandes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/type-commandes")
    @Timed
    public ResponseEntity<List<TypeCommandeDTO>> getAllTypeCommandes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TypeCommandes");
        Page<TypeCommandeDTO> page = typeCommandeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type-commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /type-commandes/:id : get the "id" typeCommande.
     *
     * @param id the id of the typeCommandeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeCommandeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/type-commandes/{id}")
    @Timed
    public ResponseEntity<TypeCommandeDTO> getTypeCommande(@PathVariable Long id) {
        log.debug("REST request to get TypeCommande : {}", id);
        TypeCommandeDTO typeCommandeDTO = typeCommandeService.findOne(id);
        return Optional.ofNullable(typeCommandeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-commandes/:id : delete the "id" typeCommande.
     *
     * @param id the id of the typeCommandeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-commandes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeCommande(@PathVariable Long id) {
        log.debug("REST request to delete TypeCommande : {}", id);
        typeCommandeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeCommande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-commandes?query=:query : search for the typeCommande corresponding
     * to the query.
     *
     * @param query the query of the typeCommande search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/type-commandes")
    @Timed
    public ResponseEntity<List<TypeCommandeDTO>> searchTypeCommandes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of TypeCommandes for query {}", query);
        Page<TypeCommandeDTO> page = typeCommandeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/type-commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
