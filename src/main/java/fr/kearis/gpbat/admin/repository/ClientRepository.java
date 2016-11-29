package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.Client;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Client entity.
 */
@SuppressWarnings("unused")
public interface ClientRepository extends JpaRepository<Client,Long> {

}
