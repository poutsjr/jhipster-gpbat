package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.AgenceClient;
import fr.kearis.gpbat.admin.repository.AgenceClientRepository;
import fr.kearis.gpbat.admin.service.AgenceClientService;
import fr.kearis.gpbat.admin.repository.search.AgenceClientSearchRepository;
import fr.kearis.gpbat.admin.service.dto.AgenceClientDTO;
import fr.kearis.gpbat.admin.service.mapper.AgenceClientMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AgenceClientResource REST controller.
 *
 * @see AgenceClientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class AgenceClientResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_SECTEUR = "AAAAAAAAAA";
    private static final String UPDATED_SECTEUR = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_CHEF_AGENCE = "AAAAAAAAAA";
    private static final String UPDATED_CHEF_AGENCE = "BBBBBBBBBB";

    private static final String DEFAULT_CHEF_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_CHEF_SERVICE = "BBBBBBBBBB";

    @Inject
    private AgenceClientRepository agenceClientRepository;

    @Inject
    private AgenceClientMapper agenceClientMapper;

    @Inject
    private AgenceClientService agenceClientService;

    @Inject
    private AgenceClientSearchRepository agenceClientSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAgenceClientMockMvc;

    private AgenceClient agenceClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgenceClientResource agenceClientResource = new AgenceClientResource();
        ReflectionTestUtils.setField(agenceClientResource, "agenceClientService", agenceClientService);
        this.restAgenceClientMockMvc = MockMvcBuilders.standaloneSetup(agenceClientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgenceClient createEntity(EntityManager em) {
        AgenceClient agenceClient = new AgenceClient()
                .nom(DEFAULT_NOM)
                .secteur(DEFAULT_SECTEUR)
                .adresse(DEFAULT_ADRESSE)
                .chefAgence(DEFAULT_CHEF_AGENCE)
                .chefService(DEFAULT_CHEF_SERVICE);
        return agenceClient;
    }

    @Before
    public void initTest() {
        agenceClientSearchRepository.deleteAll();
        agenceClient = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgenceClient() throws Exception {
        int databaseSizeBeforeCreate = agenceClientRepository.findAll().size();

        // Create the AgenceClient
        AgenceClientDTO agenceClientDTO = agenceClientMapper.agenceClientToAgenceClientDTO(agenceClient);

        restAgenceClientMockMvc.perform(post("/api/agence-clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenceClientDTO)))
                .andExpect(status().isCreated());

        // Validate the AgenceClient in the database
        List<AgenceClient> agenceClients = agenceClientRepository.findAll();
        assertThat(agenceClients).hasSize(databaseSizeBeforeCreate + 1);
        AgenceClient testAgenceClient = agenceClients.get(agenceClients.size() - 1);
        assertThat(testAgenceClient.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAgenceClient.getSecteur()).isEqualTo(DEFAULT_SECTEUR);
        assertThat(testAgenceClient.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAgenceClient.getChefAgence()).isEqualTo(DEFAULT_CHEF_AGENCE);
        assertThat(testAgenceClient.getChefService()).isEqualTo(DEFAULT_CHEF_SERVICE);

        // Validate the AgenceClient in ElasticSearch
        AgenceClient agenceClientEs = agenceClientSearchRepository.findOne(testAgenceClient.getId());
        assertThat(agenceClientEs).isEqualToComparingFieldByField(testAgenceClient);
    }

    @Test
    @Transactional
    public void getAllAgenceClients() throws Exception {
        // Initialize the database
        agenceClientRepository.saveAndFlush(agenceClient);

        // Get all the agenceClients
        restAgenceClientMockMvc.perform(get("/api/agence-clients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(agenceClient.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].secteur").value(hasItem(DEFAULT_SECTEUR.toString())))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
                .andExpect(jsonPath("$.[*].chefAgence").value(hasItem(DEFAULT_CHEF_AGENCE.toString())))
                .andExpect(jsonPath("$.[*].chefService").value(hasItem(DEFAULT_CHEF_SERVICE.toString())));
    }

    @Test
    @Transactional
    public void getAgenceClient() throws Exception {
        // Initialize the database
        agenceClientRepository.saveAndFlush(agenceClient);

        // Get the agenceClient
        restAgenceClientMockMvc.perform(get("/api/agence-clients/{id}", agenceClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agenceClient.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.secteur").value(DEFAULT_SECTEUR.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.chefAgence").value(DEFAULT_CHEF_AGENCE.toString()))
            .andExpect(jsonPath("$.chefService").value(DEFAULT_CHEF_SERVICE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgenceClient() throws Exception {
        // Get the agenceClient
        restAgenceClientMockMvc.perform(get("/api/agence-clients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgenceClient() throws Exception {
        // Initialize the database
        agenceClientRepository.saveAndFlush(agenceClient);
        agenceClientSearchRepository.save(agenceClient);
        int databaseSizeBeforeUpdate = agenceClientRepository.findAll().size();

        // Update the agenceClient
        AgenceClient updatedAgenceClient = agenceClientRepository.findOne(agenceClient.getId());
        updatedAgenceClient
                .nom(UPDATED_NOM)
                .secteur(UPDATED_SECTEUR)
                .adresse(UPDATED_ADRESSE)
                .chefAgence(UPDATED_CHEF_AGENCE)
                .chefService(UPDATED_CHEF_SERVICE);
        AgenceClientDTO agenceClientDTO = agenceClientMapper.agenceClientToAgenceClientDTO(updatedAgenceClient);

        restAgenceClientMockMvc.perform(put("/api/agence-clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenceClientDTO)))
                .andExpect(status().isOk());

        // Validate the AgenceClient in the database
        List<AgenceClient> agenceClients = agenceClientRepository.findAll();
        assertThat(agenceClients).hasSize(databaseSizeBeforeUpdate);
        AgenceClient testAgenceClient = agenceClients.get(agenceClients.size() - 1);
        assertThat(testAgenceClient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAgenceClient.getSecteur()).isEqualTo(UPDATED_SECTEUR);
        assertThat(testAgenceClient.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgenceClient.getChefAgence()).isEqualTo(UPDATED_CHEF_AGENCE);
        assertThat(testAgenceClient.getChefService()).isEqualTo(UPDATED_CHEF_SERVICE);

        // Validate the AgenceClient in ElasticSearch
        AgenceClient agenceClientEs = agenceClientSearchRepository.findOne(testAgenceClient.getId());
        assertThat(agenceClientEs).isEqualToComparingFieldByField(testAgenceClient);
    }

    @Test
    @Transactional
    public void deleteAgenceClient() throws Exception {
        // Initialize the database
        agenceClientRepository.saveAndFlush(agenceClient);
        agenceClientSearchRepository.save(agenceClient);
        int databaseSizeBeforeDelete = agenceClientRepository.findAll().size();

        // Get the agenceClient
        restAgenceClientMockMvc.perform(delete("/api/agence-clients/{id}", agenceClient.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean agenceClientExistsInEs = agenceClientSearchRepository.exists(agenceClient.getId());
        assertThat(agenceClientExistsInEs).isFalse();

        // Validate the database is empty
        List<AgenceClient> agenceClients = agenceClientRepository.findAll();
        assertThat(agenceClients).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAgenceClient() throws Exception {
        // Initialize the database
        agenceClientRepository.saveAndFlush(agenceClient);
        agenceClientSearchRepository.save(agenceClient);

        // Search the agenceClient
        restAgenceClientMockMvc.perform(get("/api/_search/agence-clients?query=id:" + agenceClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenceClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].secteur").value(hasItem(DEFAULT_SECTEUR.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].chefAgence").value(hasItem(DEFAULT_CHEF_AGENCE.toString())))
            .andExpect(jsonPath("$.[*].chefService").value(hasItem(DEFAULT_CHEF_SERVICE.toString())));
    }
}
