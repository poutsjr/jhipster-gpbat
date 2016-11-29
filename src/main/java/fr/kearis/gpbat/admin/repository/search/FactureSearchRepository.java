package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.Facture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Facture entity.
 */
public interface FactureSearchRepository extends ElasticsearchRepository<Facture, Long> {
}
