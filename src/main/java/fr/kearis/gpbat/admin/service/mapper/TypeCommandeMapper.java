package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.TypeCommandeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TypeCommande and its DTO TypeCommandeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeCommandeMapper {

    TypeCommandeDTO typeCommandeToTypeCommandeDTO(TypeCommande typeCommande);

    List<TypeCommandeDTO> typeCommandesToTypeCommandeDTOs(List<TypeCommande> typeCommandes);

    TypeCommande typeCommandeDTOToTypeCommande(TypeCommandeDTO typeCommandeDTO);

    List<TypeCommande> typeCommandeDTOsToTypeCommandes(List<TypeCommandeDTO> typeCommandeDTOs);
}
