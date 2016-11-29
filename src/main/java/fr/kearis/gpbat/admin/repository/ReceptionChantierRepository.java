package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.ReceptionChantier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReceptionChantier entity.
 */
@SuppressWarnings("unused")
public interface ReceptionChantierRepository extends JpaRepository<ReceptionChantier,Long> {

}
