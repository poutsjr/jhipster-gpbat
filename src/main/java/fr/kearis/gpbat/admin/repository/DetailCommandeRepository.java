package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.DetailCommande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DetailCommande entity.
 */
@SuppressWarnings("unused")
public interface DetailCommandeRepository extends JpaRepository<DetailCommande,Long> {

}
