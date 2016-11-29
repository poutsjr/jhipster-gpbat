package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.ProduitPartenaire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProduitPartenaire entity.
 */
@SuppressWarnings("unused")
public interface ProduitPartenaireRepository extends JpaRepository<ProduitPartenaire,Long> {

}
