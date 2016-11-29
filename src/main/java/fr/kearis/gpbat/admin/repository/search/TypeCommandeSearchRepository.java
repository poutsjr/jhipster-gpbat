package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.TypeCommande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeCommande entity.
 */
public interface TypeCommandeSearchRepository extends ElasticsearchRepository<TypeCommande, Long> {
}
