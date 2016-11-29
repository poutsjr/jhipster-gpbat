package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.TypeCommande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeCommande entity.
 */
@SuppressWarnings("unused")
public interface TypeCommandeRepository extends JpaRepository<TypeCommande,Long> {

}
