package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.CorpsEtat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CorpsEtat entity.
 */
@SuppressWarnings("unused")
public interface CorpsEtatRepository extends JpaRepository<CorpsEtat,Long> {

}
