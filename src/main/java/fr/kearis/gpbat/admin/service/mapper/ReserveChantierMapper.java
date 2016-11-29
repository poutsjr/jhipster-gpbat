package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.ReserveChantierDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ReserveChantier and its DTO ReserveChantierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReserveChantierMapper {

    @Mapping(source = "chantier.id", target = "chantierId")
    ReserveChantierDTO reserveChantierToReserveChantierDTO(ReserveChantier reserveChantier);

    List<ReserveChantierDTO> reserveChantiersToReserveChantierDTOs(List<ReserveChantier> reserveChantiers);

    @Mapping(source = "chantierId", target = "chantier")
    ReserveChantier reserveChantierDTOToReserveChantier(ReserveChantierDTO reserveChantierDTO);

    List<ReserveChantier> reserveChantierDTOsToReserveChantiers(List<ReserveChantierDTO> reserveChantierDTOs);

    default Chantier chantierFromId(Long id) {
        if (id == null) {
            return null;
        }
        Chantier chantier = new Chantier();
        chantier.setId(id);
        return chantier;
    }
}
