package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.CommandeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Commande and its DTO CommandeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommandeMapper {

    @Mapping(source = "facture.id", target = "factureId")
    @Mapping(source = "commande.id", target = "commandeId")
    @Mapping(source = "partenaire.id", target = "partenaireId")
    @Mapping(source = "typecommande.id", target = "typecommandeId")
    CommandeDTO commandeToCommandeDTO(Commande commande);

    List<CommandeDTO> commandesToCommandeDTOs(List<Commande> commandes);

    @Mapping(source = "factureId", target = "facture")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "chantiers", ignore = true)
    @Mapping(source = "commandeId", target = "commande")
    @Mapping(target = "references", ignore = true)
    @Mapping(source = "partenaireId", target = "partenaire")
    @Mapping(source = "typecommandeId", target = "typecommande")
    Commande commandeDTOToCommande(CommandeDTO commandeDTO);

    List<Commande> commandeDTOsToCommandes(List<CommandeDTO> commandeDTOs);

    default Facture factureFromId(Long id) {
        if (id == null) {
            return null;
        }
        Facture facture = new Facture();
        facture.setId(id);
        return facture;
    }

    default Commande commandeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Commande commande = new Commande();
        commande.setId(id);
        return commande;
    }

    default Partenaire partenaireFromId(Long id) {
        if (id == null) {
            return null;
        }
        Partenaire partenaire = new Partenaire();
        partenaire.setId(id);
        return partenaire;
    }

    default TypeCommande typeCommandeFromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeCommande typeCommande = new TypeCommande();
        typeCommande.setId(id);
        return typeCommande;
    }
}
