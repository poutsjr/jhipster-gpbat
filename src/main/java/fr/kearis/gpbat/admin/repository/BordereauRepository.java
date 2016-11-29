package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.Bordereau;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bordereau entity.
 */
@SuppressWarnings("unused")
public interface BordereauRepository extends JpaRepository<Bordereau,Long> {

}
