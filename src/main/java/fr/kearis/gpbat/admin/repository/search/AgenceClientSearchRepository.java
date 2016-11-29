package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.AgenceClient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AgenceClient entity.
 */
public interface AgenceClientSearchRepository extends ElasticsearchRepository<AgenceClient, Long> {
}
