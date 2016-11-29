package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.CorpsEtat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CorpsEtat entity.
 */
public interface CorpsEtatSearchRepository extends ElasticsearchRepository<CorpsEtat, Long> {
}
