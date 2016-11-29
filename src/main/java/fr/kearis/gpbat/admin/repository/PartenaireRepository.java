package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.Partenaire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Partenaire entity.
 */
@SuppressWarnings("unused")
public interface PartenaireRepository extends JpaRepository<Partenaire,Long> {

}
