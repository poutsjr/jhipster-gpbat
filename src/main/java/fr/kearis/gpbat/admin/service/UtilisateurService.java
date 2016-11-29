package fr.kearis.gpbat.admin.service;

import fr.kearis.gpbat.admin.domain.Utilisateur;
import fr.kearis.gpbat.admin.repository.UtilisateurRepository;
import fr.kearis.gpbat.admin.repository.search.UtilisateurSearchRepository;
import fr.kearis.gpbat.admin.service.dto.UtilisateurDTO;
import fr.kearis.gpbat.admin.service.mapper.UtilisateurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Utilisateur.
 */
@Service
@Transactional
public class UtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurService.class);
    
    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private UtilisateurMapper utilisateurMapper;

    @Inject
    private UtilisateurSearchRepository utilisateurSearchRepository;

    /**
     * Save a utilisateur.
     *
     * @param utilisateurDTO the entity to save
     * @return the persisted entity
     */
    public UtilisateurDTO save(UtilisateurDTO utilisateurDTO) {
        log.debug("Request to save Utilisateur : {}", utilisateurDTO);
        Utilisateur utilisateur = utilisateurMapper.utilisateurDTOToUtilisateur(utilisateurDTO);
        utilisateur = utilisateurRepository.save(utilisateur);
        UtilisateurDTO result = utilisateurMapper.utilisateurToUtilisateurDTO(utilisateur);
        utilisateurSearchRepository.save(utilisateur);
        return result;
    }

    /**
     *  Get all the utilisateurs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<UtilisateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Utilisateurs");
        Page<Utilisateur> result = utilisateurRepository.findAll(pageable);
        return result.map(utilisateur -> utilisateurMapper.utilisateurToUtilisateurDTO(utilisateur));
    }

    /**
     *  Get one utilisateur by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public UtilisateurDTO findOne(Long id) {
        log.debug("Request to get Utilisateur : {}", id);
        Utilisateur utilisateur = utilisateurRepository.findOne(id);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.utilisateurToUtilisateurDTO(utilisateur);
        return utilisateurDTO;
    }

    /**
     *  Delete the  utilisateur by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.delete(id);
        utilisateurSearchRepository.delete(id);
    }

    /**
     * Search for the utilisateur corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UtilisateurDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Utilisateurs for query {}", query);
        Page<Utilisateur> result = utilisateurSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(utilisateur -> utilisateurMapper.utilisateurToUtilisateurDTO(utilisateur));
    }
}
