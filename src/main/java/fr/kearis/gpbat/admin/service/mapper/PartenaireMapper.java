package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.PartenaireDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Partenaire and its DTO PartenaireDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PartenaireMapper {

    PartenaireDTO partenaireToPartenaireDTO(Partenaire partenaire);

    List<PartenaireDTO> partenairesToPartenaireDTOs(List<Partenaire> partenaires);

    Partenaire partenaireDTOToPartenaire(PartenaireDTO partenaireDTO);

    List<Partenaire> partenaireDTOsToPartenaires(List<PartenaireDTO> partenaireDTOs);
}
