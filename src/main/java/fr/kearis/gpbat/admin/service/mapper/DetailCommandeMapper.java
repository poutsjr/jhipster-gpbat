package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.DetailCommandeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity DetailCommande and its DTO DetailCommandeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DetailCommandeMapper {

    @Mapping(source = "simulation.id", target = "simulationId")
    @Mapping(source = "commande.id", target = "commandeId")
    @Mapping(source = "bordereau.id", target = "bordereauId")
    DetailCommandeDTO detailCommandeToDetailCommandeDTO(DetailCommande detailCommande);

    List<DetailCommandeDTO> detailCommandesToDetailCommandeDTOs(List<DetailCommande> detailCommandes);

    @Mapping(source = "simulationId", target = "simulation")
    @Mapping(source = "commandeId", target = "commande")
    @Mapping(source = "bordereauId", target = "bordereau")
    DetailCommande detailCommandeDTOToDetailCommande(DetailCommandeDTO detailCommandeDTO);

    List<DetailCommande> detailCommandeDTOsToDetailCommandes(List<DetailCommandeDTO> detailCommandeDTOs);

    default Simulation simulationFromId(Long id) {
        if (id == null) {
            return null;
        }
        Simulation simulation = new Simulation();
        simulation.setId(id);
        return simulation;
    }

    default Commande commandeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Commande commande = new Commande();
        commande.setId(id);
        return commande;
    }

    default Bordereau bordereauFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bordereau bordereau = new Bordereau();
        bordereau.setId(id);
        return bordereau;
    }
}
