package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.Utilisateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Utilisateur entity.
 */
public interface UtilisateurSearchRepository extends ElasticsearchRepository<Utilisateur, Long> {
}
