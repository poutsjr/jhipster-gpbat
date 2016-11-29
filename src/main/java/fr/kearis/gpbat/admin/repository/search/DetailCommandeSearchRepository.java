package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.DetailCommande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DetailCommande entity.
 */
public interface DetailCommandeSearchRepository extends ElasticsearchRepository<DetailCommande, Long> {
}
