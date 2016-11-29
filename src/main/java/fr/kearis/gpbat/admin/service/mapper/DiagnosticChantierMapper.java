package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.DiagnosticChantierDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity DiagnosticChantier and its DTO DiagnosticChantierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DiagnosticChantierMapper {

    @Mapping(source = "chantier.id", target = "chantierId")
    DiagnosticChantierDTO diagnosticChantierToDiagnosticChantierDTO(DiagnosticChantier diagnosticChantier);

    List<DiagnosticChantierDTO> diagnosticChantiersToDiagnosticChantierDTOs(List<DiagnosticChantier> diagnosticChantiers);

    @Mapping(source = "chantierId", target = "chantier")
    DiagnosticChantier diagnosticChantierDTOToDiagnosticChantier(DiagnosticChantierDTO diagnosticChantierDTO);

    List<DiagnosticChantier> diagnosticChantierDTOsToDiagnosticChantiers(List<DiagnosticChantierDTO> diagnosticChantierDTOs);

    default Chantier chantierFromId(Long id) {
        if (id == null) {
            return null;
        }
        Chantier chantier = new Chantier();
        chantier.setId(id);
        return chantier;
    }
}
