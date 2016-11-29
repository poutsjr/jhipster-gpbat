package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.Commande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Commande entity.
 */
public interface CommandeSearchRepository extends ElasticsearchRepository<Commande, Long> {
}
