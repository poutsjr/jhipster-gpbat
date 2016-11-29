package fr.kearis.gpbat.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.kearis.gpbat.admin.service.CommandeService;
import fr.kearis.gpbat.admin.web.rest.util.HeaderUtil;
import fr.kearis.gpbat.admin.web.rest.util.PaginationUtil;
import fr.kearis.gpbat.admin.service.dto.CommandeDTO;
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
 * REST controller for managing Commande.
 */
@RestController
@RequestMapping("/api")
public class CommandeResource {

    private final Logger log = LoggerFactory.getLogger(CommandeResource.class);
        
    @Inject
    private CommandeService commandeService;

    /**
     * POST  /commandes : Create a new commande.
     *
     * @param commandeDTO the commandeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commandeDTO, or with status 400 (Bad Request) if the commande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commandes")
    @Timed
    public ResponseEntity<CommandeDTO> createCommande(@RequestBody CommandeDTO commandeDTO) throws URISyntaxException {
        log.debug("REST request to save Commande : {}", commandeDTO);
        if (commandeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commande", "idexists", "A new commande cannot already have an ID")).body(null);
        }
        CommandeDTO result = commandeService.save(commandeDTO);
        return ResponseEntity.created(new URI("/api/commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commandes : Updates an existing commande.
     *
     * @param commandeDTO the commandeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commandeDTO,
     * or with status 400 (Bad Request) if the commandeDTO is not valid,
     * or with status 500 (Internal Server Error) if the commandeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commandes")
    @Timed
    public ResponseEntity<CommandeDTO> updateCommande(@RequestBody CommandeDTO commandeDTO) throws URISyntaxException {
        log.debug("REST request to update Commande : {}", commandeDTO);
        if (commandeDTO.getId() == null) {
            return createCommande(commandeDTO);
        }
        CommandeDTO result = commandeService.save(commandeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commande", commandeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commandes : get all the commandes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commandes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/commandes")
    @Timed
    public ResponseEntity<List<CommandeDTO>> getAllCommandes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Commandes");
        Page<CommandeDTO> page = commandeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commandes/:id : get the "id" commande.
     *
     * @param id the id of the commandeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commandeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/commandes/{id}")
    @Timed
    public ResponseEntity<CommandeDTO> getCommande(@PathVariable Long id) {
        log.debug("REST request to get Commande : {}", id);
        CommandeDTO commandeDTO = commandeService.findOne(id);
        return Optional.ofNullable(commandeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commandes/:id : delete the "id" commande.
     *
     * @param id the id of the commandeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commandes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        log.debug("REST request to delete Commande : {}", id);
        commandeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/commandes?query=:query : search for the commande corresponding
     * to the query.
     *
     * @param query the query of the commande search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/commandes")
    @Timed
    public ResponseEntity<List<CommandeDTO>> searchCommandes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Commandes for query {}", query);
        Page<CommandeDTO> page = commandeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
