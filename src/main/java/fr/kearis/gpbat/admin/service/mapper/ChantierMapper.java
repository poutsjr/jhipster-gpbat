package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.ChantierDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Chantier and its DTO ChantierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChantierMapper {

    @Mapping(source = "reception.id", target = "receptionId")
    @Mapping(source = "pilote.id", target = "piloteId")
    @Mapping(source = "commande.id", target = "commandeId")
    ChantierDTO chantierToChantierDTO(Chantier chantier);

    List<ChantierDTO> chantiersToChantierDTOs(List<Chantier> chantiers);

    @Mapping(source = "receptionId", target = "reception")
    @Mapping(target = "avancements", ignore = true)
    @Mapping(target = "reserves", ignore = true)
    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "diagnostics", ignore = true)
    @Mapping(source = "piloteId", target = "pilote")
    @Mapping(source = "commandeId", target = "commande")
    Chantier chantierDTOToChantier(ChantierDTO chantierDTO);

    List<Chantier> chantierDTOsToChantiers(List<ChantierDTO> chantierDTOs);

    default ReceptionChantier receptionChantierFromId(Long id) {
        if (id == null) {
            return null;
        }
        ReceptionChantier receptionChantier = new ReceptionChantier();
        receptionChantier.setId(id);
        return receptionChantier;
    }

    default Utilisateur utilisateurFromId(Long id) {
        if (id == null) {
            return null;
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        return utilisateur;
    }

    default Commande commandeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Commande commande = new Commande();
        commande.setId(id);
        return commande;
    }
}
