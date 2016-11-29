package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.DiagnosticChantier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DiagnosticChantier entity.
 */
public interface DiagnosticChantierSearchRepository extends ElasticsearchRepository<DiagnosticChantier, Long> {
}
