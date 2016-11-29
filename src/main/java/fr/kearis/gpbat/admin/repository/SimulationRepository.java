package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.Simulation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Simulation entity.
 */
@SuppressWarnings("unused")
public interface SimulationRepository extends JpaRepository<Simulation,Long> {

}
