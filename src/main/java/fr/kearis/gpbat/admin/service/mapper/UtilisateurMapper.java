package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.UtilisateurDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Utilisateur and its DTO UtilisateurDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UtilisateurMapper {

    UtilisateurDTO utilisateurToUtilisateurDTO(Utilisateur utilisateur);

    List<UtilisateurDTO> utilisateursToUtilisateurDTOs(List<Utilisateur> utilisateurs);

    Utilisateur utilisateurDTOToUtilisateur(UtilisateurDTO utilisateurDTO);

    List<Utilisateur> utilisateurDTOsToUtilisateurs(List<UtilisateurDTO> utilisateurDTOs);
}
