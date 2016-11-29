package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.ProduitPartenaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProduitPartenaire entity.
 */
public interface ProduitPartenaireSearchRepository extends ElasticsearchRepository<ProduitPartenaire, Long> {
}
