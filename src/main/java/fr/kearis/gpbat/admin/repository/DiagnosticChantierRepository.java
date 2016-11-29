package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.DiagnosticChantier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DiagnosticChantier entity.
 */
@SuppressWarnings("unused")
public interface DiagnosticChantierRepository extends JpaRepository<DiagnosticChantier,Long> {

}
