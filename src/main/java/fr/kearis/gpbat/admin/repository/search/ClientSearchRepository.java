package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.Client;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Client entity.
 */
public interface ClientSearchRepository extends ElasticsearchRepository<Client, Long> {
}
