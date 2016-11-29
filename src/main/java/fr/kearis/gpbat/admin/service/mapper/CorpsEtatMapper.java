package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.CorpsEtatDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CorpsEtat and its DTO CorpsEtatDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CorpsEtatMapper {

    CorpsEtatDTO corpsEtatToCorpsEtatDTO(CorpsEtat corpsEtat);

    List<CorpsEtatDTO> corpsEtatsToCorpsEtatDTOs(List<CorpsEtat> corpsEtats);

    CorpsEtat corpsEtatDTOToCorpsEtat(CorpsEtatDTO corpsEtatDTO);

    List<CorpsEtat> corpsEtatDTOsToCorpsEtats(List<CorpsEtatDTO> corpsEtatDTOs);
}
