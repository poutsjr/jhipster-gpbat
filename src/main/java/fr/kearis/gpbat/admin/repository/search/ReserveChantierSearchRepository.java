package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.ReserveChantier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ReserveChantier entity.
 */
public interface ReserveChantierSearchRepository extends ElasticsearchRepository<ReserveChantier, Long> {
}
