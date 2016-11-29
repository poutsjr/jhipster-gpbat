package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.ReceptionChantier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ReceptionChantier entity.
 */
public interface ReceptionChantierSearchRepository extends ElasticsearchRepository<ReceptionChantier, Long> {
}
