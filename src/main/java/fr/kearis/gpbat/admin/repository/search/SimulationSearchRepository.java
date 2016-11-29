package fr.kearis.gpbat.admin.repository.search;

import fr.kearis.gpbat.admin.domain.Simulation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Simulation entity.
 */
public interface SimulationSearchRepository extends ElasticsearchRepository<Simulation, Long> {
}
