package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.AvancementChantier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AvancementChantier entity.
 */
public interface AvancementChantierSearchRepository extends ElasticsearchRepository<AvancementChantier, Long> {
}
