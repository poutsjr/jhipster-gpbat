package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.Facture;
import fr.kearis.gpbat.admin.repository.FactureRepository;
import fr.kearis.gpbat.admin.service.FactureService;
import fr.kearis.gpbat.admin.repository.search.FactureSearchRepository;
import fr.kearis.gpbat.admin.service.dto.FactureDTO;
import fr.kearis.gpbat.admin.service.mapper.FactureMapper;

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
 * Test class for the FactureResource REST controller.
 *
 * @see FactureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class FactureResourceIntTest {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    @Inject
    private FactureRepository factureRepository;

    @Inject
    private FactureMapper factureMapper;

    @Inject
    private FactureService factureService;

    @Inject
    private FactureSearchRepository factureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFactureMockMvc;

    private Facture facture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FactureResource factureResource = new FactureResource();
        ReflectionTestUtils.setField(factureResource, "factureService", factureService);
        this.restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
                .numero(DEFAULT_NUMERO);
        return facture;
    }

    @Before
    public void initTest() {
        factureSearchRepository.deleteAll();
        facture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture
        FactureDTO factureDTO = factureMapper.factureToFactureDTO(facture);

        restFactureMockMvc.perform(post("/api/factures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
                .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factures = factureRepository.findAll();
        assertThat(factures).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factures.get(factures.size() - 1);
        assertThat(testFacture.getNumero()).isEqualTo(DEFAULT_NUMERO);

        // Validate the Facture in ElasticSearch
        Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        assertThat(factureEs).isEqualToComparingFieldByField(testFacture);
    }

    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factures
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())));
    }

    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findOne(facture.getId());
        updatedFacture
                .numero(UPDATED_NUMERO);
        FactureDTO factureDTO = factureMapper.factureToFactureDTO(updatedFacture);

        restFactureMockMvc.perform(put("/api/factures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
                .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factures = factureRepository.findAll();
        assertThat(factures).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factures.get(factures.size() - 1);
        assertThat(testFacture.getNumero()).isEqualTo(UPDATED_NUMERO);

        // Validate the Facture in ElasticSearch
        Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        assertThat(factureEs).isEqualToComparingFieldByField(testFacture);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);
        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Get the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean factureExistsInEs = factureSearchRepository.exists(facture.getId());
        assertThat(factureExistsInEs).isFalse();

        // Validate the database is empty
        List<Facture> factures = factureRepository.findAll();
        assertThat(factures).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);

        // Search the facture
        restFactureMockMvc.perform(get("/api/_search/factures?query=id:" + facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())));
    }
}
