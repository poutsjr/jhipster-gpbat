package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.ReceptionChantierDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ReceptionChantier and its DTO ReceptionChantierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReceptionChantierMapper {

    ReceptionChantierDTO receptionChantierToReceptionChantierDTO(ReceptionChantier receptionChantier);

    List<ReceptionChantierDTO> receptionChantiersToReceptionChantierDTOs(List<ReceptionChantier> receptionChantiers);

    ReceptionChantier receptionChantierDTOToReceptionChantier(ReceptionChantierDTO receptionChantierDTO);

    List<ReceptionChantier> receptionChantierDTOsToReceptionChantiers(List<ReceptionChantierDTO> receptionChantierDTOs);
}
