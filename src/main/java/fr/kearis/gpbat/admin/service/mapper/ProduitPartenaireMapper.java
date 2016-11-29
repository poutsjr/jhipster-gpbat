package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.ProduitPartenaireDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProduitPartenaire and its DTO ProduitPartenaireDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProduitPartenaireMapper {

    @Mapping(source = "bordereau.id", target = "bordereauId")
    ProduitPartenaireDTO produitPartenaireToProduitPartenaireDTO(ProduitPartenaire produitPartenaire);

    List<ProduitPartenaireDTO> produitPartenairesToProduitPartenaireDTOs(List<ProduitPartenaire> produitPartenaires);

    @Mapping(source = "bordereauId", target = "bordereau")
    ProduitPartenaire produitPartenaireDTOToProduitPartenaire(ProduitPartenaireDTO produitPartenaireDTO);

    List<ProduitPartenaire> produitPartenaireDTOsToProduitPartenaires(List<ProduitPartenaireDTO> produitPartenaireDTOs);

    default Bordereau bordereauFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bordereau bordereau = new Bordereau();
        bordereau.setId(id);
        return bordereau;
    }
}
