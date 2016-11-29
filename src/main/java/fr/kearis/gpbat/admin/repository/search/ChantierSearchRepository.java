package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.Chantier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Chantier entity.
 */
public interface ChantierSearchRepository extends ElasticsearchRepository<Chantier, Long> {
}
