package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.Facture;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Facture entity.
 */
@SuppressWarnings("unused")
public interface FactureRepository extends JpaRepository<Facture,Long> {

}
