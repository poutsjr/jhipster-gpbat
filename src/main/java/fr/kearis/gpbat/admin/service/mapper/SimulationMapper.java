package fr.kearis.gpbat.admin.service.mapper;

import fr.kearis.gpbat.admin.domain.*;
import fr.kearis.gpbat.admin.service.dto.SimulationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Simulation and its DTO SimulationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SimulationMapper {

    SimulationDTO simulationToSimulationDTO(Simulation simulation);

    List<SimulationDTO> simulationsToSimulationDTOs(List<Simulation> simulations);

    @Mapping(target = "details", ignore = true)
    Simulation simulationDTOToSimulation(SimulationDTO simulationDTO);

    List<Simulation> simulationDTOsToSimulations(List<SimulationDTO> simulationDTOs);
}
