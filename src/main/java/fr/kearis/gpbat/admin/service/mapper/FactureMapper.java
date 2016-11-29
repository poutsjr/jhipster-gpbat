package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.FactureDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Facture and its DTO FactureDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FactureMapper {

    FactureDTO factureToFactureDTO(Facture facture);

    List<FactureDTO> facturesToFactureDTOs(List<Facture> factures);

    Facture factureDTOToFacture(FactureDTO factureDTO);

    List<Facture> factureDTOsToFactures(List<FactureDTO> factureDTOs);
}
