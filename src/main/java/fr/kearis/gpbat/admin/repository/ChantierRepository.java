package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.Chantier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Chantier entity.
 */
@SuppressWarnings("unused")
public interface ChantierRepository extends JpaRepository<Chantier,Long> {

}
