package fr.kearis.gpbat.admin.repository;

import fr.kearis.gpbat.admin.domain.AvancementChantier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AvancementChantier entity.
 */
@SuppressWarnings("unused")
public interface AvancementChantierRepository extends JpaRepository<AvancementChantier,Long> {

}
