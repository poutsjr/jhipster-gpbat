package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.AgenceClient;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AgenceClient entity.
 */
@SuppressWarnings("unused")
public interface AgenceClientRepository extends JpaRepository<AgenceClient,Long> {

}
