package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.ProduitPartenaireService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.ProduitPartenaireDTO;
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
 * REST controller for managing ProduitPartenaire.
 */
@RestController
@RequestMapping("/api")
public class ProduitPartenaireResource {

    private final Logger log = LoggerFactory.getLogger(ProduitPartenaireResource.class);
        
    @Inject
    private ProduitPartenaireService produitPartenaireService;

    /**
     * POST  /produit-partenaires : Create a new produitPartenaire.
     *
     * @param produitPartenaireDTO the produitPartenaireDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new produitPartenaireDTO, or with status 400 (Bad Request) if the produitPartenaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/produit-partenaires")
    @Timed
    public ResponseEntity<ProduitPartenaireDTO> createProduitPartenaire(@RequestBody ProduitPartenaireDTO produitPartenaireDTO) throws URISyntaxException {
        log.debug("REST request to save ProduitPartenaire : {}", produitPartenaireDTO);
        if (produitPartenaireDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("produitPartenaire", "idexists", "A new produitPartenaire cannot already have an ID")).body(null);
        }
        ProduitPartenaireDTO result = produitPartenaireService.save(produitPartenaireDTO);
        return ResponseEntity.created(new URI("/api/produit-partenaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("produitPartenaire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /produit-partenaires : Updates an existing produitPartenaire.
     *
     * @param produitPartenaireDTO the produitPartenaireDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated produitPartenaireDTO,
     * or with status 400 (Bad Request) if the produitPartenaireDTO is not valid,
     * or with status 500 (Internal Server Error) if the produitPartenaireDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/produit-partenaires")
    @Timed
    public ResponseEntity<ProduitPartenaireDTO> updateProduitPartenaire(@RequestBody ProduitPartenaireDTO produitPartenaireDTO) throws URISyntaxException {
        log.debug("REST request to update ProduitPartenaire : {}", produitPartenaireDTO);
        if (produitPartenaireDTO.getId() == null) {
            return createProduitPartenaire(produitPartenaireDTO);
        }
        ProduitPartenaireDTO result = produitPartenaireService.save(produitPartenaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("produitPartenaire", produitPartenaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /produit-partenaires : get all the produitPartenaires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of produitPartenaires in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/produit-partenaires")
    @Timed
    public ResponseEntity<List<ProduitPartenaireDTO>> getAllProduitPartenaires(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProduitPartenaires");
        Page<ProduitPartenaireDTO> page = produitPartenaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/produit-partenaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /produit-partenaires/:id : get the "id" produitPartenaire.
     *
     * @param id the id of the produitPartenaireDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the produitPartenaireDTO, or with status 404 (Not Found)
     */
    @GetMapping("/produit-partenaires/{id}")
    @Timed
    public ResponseEntity<ProduitPartenaireDTO> getProduitPartenaire(@PathVariable Long id) {
        log.debug("REST request to get ProduitPartenaire : {}", id);
        ProduitPartenaireDTO produitPartenaireDTO = produitPartenaireService.findOne(id);
        return Optional.ofNullable(produitPartenaireDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /produit-partenaires/:id : delete the "id" produitPartenaire.
     *
     * @param id the id of the produitPartenaireDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/produit-partenaires/{id}")
    @Timed
    public ResponseEntity<Void> deleteProduitPartenaire(@PathVariable Long id) {
        log.debug("REST request to delete ProduitPartenaire : {}", id);
        produitPartenaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("produitPartenaire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/produit-partenaires?query=:query : search for the produitPartenaire corresponding
     * to the query.
     *
     * @param query the query of the produitPartenaire search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/produit-partenaires")
    @Timed
    public ResponseEntity<List<ProduitPartenaireDTO>> searchProduitPartenaires(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ProduitPartenaires for query {}", query);
        Page<ProduitPartenaireDTO> page = produitPartenaireService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/produit-partenaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
