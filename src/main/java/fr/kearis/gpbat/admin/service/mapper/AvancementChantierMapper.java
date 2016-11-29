package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.AvancementChantierDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AvancementChantier and its DTO AvancementChantierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AvancementChantierMapper {

    @Mapping(source = "chantier.id", target = "chantierId")
    AvancementChantierDTO avancementChantierToAvancementChantierDTO(AvancementChantier avancementChantier);

    List<AvancementChantierDTO> avancementChantiersToAvancementChantierDTOs(List<AvancementChantier> avancementChantiers);

    @Mapping(source = "chantierId", target = "chantier")
    AvancementChantier avancementChantierDTOToAvancementChantier(AvancementChantierDTO avancementChantierDTO);

    List<AvancementChantier> avancementChantierDTOsToAvancementChantiers(List<AvancementChantierDTO> avancementChantierDTOs);

    default Chantier chantierFromId(Long id) {
        if (id == null) {
            return null;
        }
        Chantier chantier = new Chantier();
        chantier.setId(id);
        return chantier;
    }
}
