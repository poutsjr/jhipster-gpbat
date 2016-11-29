package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.AgenceClientDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AgenceClient and its DTO AgenceClientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgenceClientMapper {

    @Mapping(source = "client.id", target = "clientId")
    AgenceClientDTO agenceClientToAgenceClientDTO(AgenceClient agenceClient);

    List<AgenceClientDTO> agenceClientsToAgenceClientDTOs(List<AgenceClient> agenceClients);

    @Mapping(source = "clientId", target = "client")
    AgenceClient agenceClientDTOToAgenceClient(AgenceClientDTO agenceClientDTO);

    List<AgenceClient> agenceClientDTOsToAgenceClients(List<AgenceClientDTO> agenceClientDTOs);

    default Client clientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
