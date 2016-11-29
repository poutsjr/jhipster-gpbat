package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.Bordereau;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bordereau entity.
 */
public interface BordereauSearchRepository extends ElasticsearchRepository<Bordereau, Long> {
}
