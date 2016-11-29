package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.UtilisateurService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.UtilisateurDTO;
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
 * REST controller for managing Utilisateur.
 */
@RestController
@RequestMapping("/api")
public class UtilisateurResource {

    private final Logger log = LoggerFactory.getLogger(UtilisateurResource.class);
        
    @Inject
    private UtilisateurService utilisateurService;

    /**
     * POST  /utilisateurs : Create a new utilisateur.
     *
     * @param utilisateurDTO the utilisateurDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new utilisateurDTO, or with status 400 (Bad Request) if the utilisateur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/utilisateurs")
    @Timed
    public ResponseEntity<UtilisateurDTO> createUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) throws URISyntaxException {
        log.debug("REST request to save Utilisateur : {}", utilisateurDTO);
        if (utilisateurDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("utilisateur", "idexists", "A new utilisateur cannot already have an ID")).body(null);
        }
        UtilisateurDTO result = utilisateurService.save(utilisateurDTO);
        return ResponseEntity.created(new URI("/api/utilisateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("utilisateur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /utilisateurs : Updates an existing utilisateur.
     *
     * @param utilisateurDTO the utilisateurDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated utilisateurDTO,
     * or with status 400 (Bad Request) if the utilisateurDTO is not valid,
     * or with status 500 (Internal Server Error) if the utilisateurDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/utilisateurs")
    @Timed
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) throws URISyntaxException {
        log.debug("REST request to update Utilisateur : {}", utilisateurDTO);
        if (utilisateurDTO.getId() == null) {
            return createUtilisateur(utilisateurDTO);
        }
        UtilisateurDTO result = utilisateurService.save(utilisateurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("utilisateur", utilisateurDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /utilisateurs : get all the utilisateurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of utilisateurs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/utilisateurs")
    @Timed
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Utilisateurs");
        Page<UtilisateurDTO> page = utilisateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/utilisateurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /utilisateurs/:id : get the "id" utilisateur.
     *
     * @param id the id of the utilisateurDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the utilisateurDTO, or with status 404 (Not Found)
     */
    @GetMapping("/utilisateurs/{id}")
    @Timed
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable Long id) {
        log.debug("REST request to get Utilisateur : {}", id);
        UtilisateurDTO utilisateurDTO = utilisateurService.findOne(id);
        return Optional.ofNullable(utilisateurDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /utilisateurs/:id : delete the "id" utilisateur.
     *
     * @param id the id of the utilisateurDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/utilisateurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        log.debug("REST request to delete Utilisateur : {}", id);
        utilisateurService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("utilisateur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/utilisateurs?query=:query : search for the utilisateur corresponding
     * to the query.
     *
     * @param query the query of the utilisateur search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/utilisateurs")
    @Timed
    public ResponseEntity<List<UtilisateurDTO>> searchUtilisateurs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Utilisateurs for query {}", query);
        Page<UtilisateurDTO> page = utilisateurService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/utilisateurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
