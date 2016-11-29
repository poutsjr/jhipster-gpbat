package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.ClientDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Client and its DTO ClientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientMapper {

    @Mapping(source = "chantier.id", target = "chantierId")
    ClientDTO clientToClientDTO(Client client);

    List<ClientDTO> clientsToClientDTOs(List<Client> clients);

    @Mapping(source = "chantierId", target = "chantier")
    @Mapping(target = "agences", ignore = true)
    Client clientDTOToClient(ClientDTO clientDTO);

    List<Client> clientDTOsToClients(List<ClientDTO> clientDTOs);

    default Chantier chantierFromId(Long id) {
        if (id == null) {
            return null;
        }
        Chantier chantier = new Chantier();
        chantier.setId(id);
        return chantier;
    }
}
