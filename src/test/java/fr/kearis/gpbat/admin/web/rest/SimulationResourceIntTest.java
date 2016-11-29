package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.Simulation;
import fr.kearis.gpbat.admin.repository.SimulationRepository;
import fr.kearis.gpbat.admin.service.SimulationService;
import fr.kearis.gpbat.admin.repository.search.SimulationSearchRepository;
import fr.kearis.gpbat.admin.service.dto.SimulationDTO;
import fr.kearis.gpbat.admin.service.mapper.SimulationMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.kearis.gpbat.admin.domain.enumeration.EtatSimulation;
/**
 * Test class for the SimulationResource REST controller.
 *
 * @see SimulationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class SimulationResourceIntTest {

    private static final LocalDate DEFAULT_DATE_SIMULATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SIMULATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REMARQUES = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUES = "BBBBBBBBBB";

    private static final EtatSimulation DEFAULT_ETAT = EtatSimulation.BROUILLON;
    private static final EtatSimulation UPDATED_ETAT = EtatSimulation.DEVIS;

    @Inject
    private SimulationRepository simulationRepository;

    @Inject
    private SimulationMapper simulationMapper;

    @Inject
    private SimulationService simulationService;

    @Inject
    private SimulationSearchRepository simulationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSimulationMockMvc;

    private Simulation simulation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SimulationResource simulationResource = new SimulationResource();
        ReflectionTestUtils.setField(simulationResource, "simulationService", simulationService);
        this.restSimulationMockMvc = MockMvcBuilders.standaloneSetup(simulationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Simulation createEntity(EntityManager em) {
        Simulation simulation = new Simulation()
                .dateSimulation(DEFAULT_DATE_SIMULATION)
                .remarques(DEFAULT_REMARQUES)
                .etat(DEFAULT_ETAT);
        return simulation;
    }

    @Before
    public void initTest() {
        simulationSearchRepository.deleteAll();
        simulation = createEntity(em);
    }

    @Test
    @Transactional
    public void createSimulation() throws Exception {
        int databaseSizeBeforeCreate = simulationRepository.findAll().size();

        // Create the Simulation
        SimulationDTO simulationDTO = simulationMapper.simulationToSimulationDTO(simulation);

        restSimulationMockMvc.perform(post("/api/simulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(simulationDTO)))
                .andExpect(status().isCreated());

        // Validate the Simulation in the database
        List<Simulation> simulations = simulationRepository.findAll();
        assertThat(simulations).hasSize(databaseSizeBeforeCreate + 1);
        Simulation testSimulation = simulations.get(simulations.size() - 1);
        assertThat(testSimulation.getDateSimulation()).isEqualTo(DEFAULT_DATE_SIMULATION);
        assertThat(testSimulation.getRemarques()).isEqualTo(DEFAULT_REMARQUES);
        assertThat(testSimulation.getEtat()).isEqualTo(DEFAULT_ETAT);

        // Validate the Simulation in ElasticSearch
        Simulation simulationEs = simulationSearchRepository.findOne(testSimulation.getId());
        assertThat(simulationEs).isEqualToComparingFieldByField(testSimulation);
    }

    @Test
    @Transactional
    public void getAllSimulations() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulations
        restSimulationMockMvc.perform(get("/api/simulations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(simulation.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateSimulation").value(hasItem(DEFAULT_DATE_SIMULATION.toString())))
                .andExpect(jsonPath("$.[*].remarques").value(hasItem(DEFAULT_REMARQUES.toString())))
                .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void getSimulation() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get the simulation
        restSimulationMockMvc.perform(get("/api/simulations/{id}", simulation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(simulation.getId().intValue()))
            .andExpect(jsonPath("$.dateSimulation").value(DEFAULT_DATE_SIMULATION.toString()))
            .andExpect(jsonPath("$.remarques").value(DEFAULT_REMARQUES.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSimulation() throws Exception {
        // Get the simulation
        restSimulationMockMvc.perform(get("/api/simulations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSimulation() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);
        simulationSearchRepository.save(simulation);
        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();

        // Update the simulation
        Simulation updatedSimulation = simulationRepository.findOne(simulation.getId());
        updatedSimulation
                .dateSimulation(UPDATED_DATE_SIMULATION)
                .remarques(UPDATED_REMARQUES)
                .etat(UPDATED_ETAT);
        SimulationDTO simulationDTO = simulationMapper.simulationToSimulationDTO(updatedSimulation);

        restSimulationMockMvc.perform(put("/api/simulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(simulationDTO)))
                .andExpect(status().isOk());

        // Validate the Simulation in the database
        List<Simulation> simulations = simulationRepository.findAll();
        assertThat(simulations).hasSize(databaseSizeBeforeUpdate);
        Simulation testSimulation = simulations.get(simulations.size() - 1);
        assertThat(testSimulation.getDateSimulation()).isEqualTo(UPDATED_DATE_SIMULATION);
        assertThat(testSimulation.getRemarques()).isEqualTo(UPDATED_REMARQUES);
        assertThat(testSimulation.getEtat()).isEqualTo(UPDATED_ETAT);

        // Validate the Simulation in ElasticSearch
        Simulation simulationEs = simulationSearchRepository.findOne(testSimulation.getId());
        assertThat(simulationEs).isEqualToComparingFieldByField(testSimulation);
    }

    @Test
    @Transactional
    public void deleteSimulation() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);
        simulationSearchRepository.save(simulation);
        int databaseSizeBeforeDelete = simulationRepository.findAll().size();

        // Get the simulation
        restSimulationMockMvc.perform(delete("/api/simulations/{id}", simulation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean simulationExistsInEs = simulationSearchRepository.exists(simulation.getId());
        assertThat(simulationExistsInEs).isFalse();

        // Validate the database is empty
        List<Simulation> simulations = simulationRepository.findAll();
        assertThat(simulations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSimulation() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);
        simulationSearchRepository.save(simulation);

        // Search the simulation
        restSimulationMockMvc.perform(get("/api/_search/simulations?query=id:" + simulation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simulation.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateSimulation").value(hasItem(DEFAULT_DATE_SIMULATION.toString())))
            .andExpect(jsonPath("$.[*].remarques").value(hasItem(DEFAULT_REMARQUES.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }
}
