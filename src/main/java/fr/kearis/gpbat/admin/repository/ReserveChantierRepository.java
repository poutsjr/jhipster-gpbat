package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.ReserveChantier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReserveChantier entity.
 */
@SuppressWarnings("unused")
public interface ReserveChantierRepository extends JpaRepository<ReserveChantier,Long> {

}
