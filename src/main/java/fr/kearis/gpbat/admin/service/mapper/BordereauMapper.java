package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.BordereauDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Bordereau and its DTO BordereauDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BordereauMapper {

    @Mapping(source = "corpsEtat.id", target = "corpsEtatId")
    BordereauDTO bordereauToBordereauDTO(Bordereau bordereau);

    List<BordereauDTO> bordereausToBordereauDTOs(List<Bordereau> bordereaus);

    @Mapping(source = "corpsEtatId", target = "corpsEtat")
    Bordereau bordereauDTOToBordereau(BordereauDTO bordereauDTO);

    List<Bordereau> bordereauDTOsToBordereaus(List<BordereauDTO> bordereauDTOs);

    default CorpsEtat corpsEtatFromId(Long id) {
        if (id == null) {
            return null;
        }
        CorpsEtat corpsEtat = new CorpsEtat();
        corpsEtat.setId(id);
        return corpsEtat;
    }
}
