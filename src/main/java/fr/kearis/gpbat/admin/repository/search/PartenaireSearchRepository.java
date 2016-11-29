package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.Partenaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Partenaire entity.
 */
public interface PartenaireSearchRepository extends ElasticsearchRepository<Partenaire, Long> {
}
