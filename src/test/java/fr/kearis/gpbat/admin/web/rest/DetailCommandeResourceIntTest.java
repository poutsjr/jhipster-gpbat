package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.DetailCommande;
import fr.kearis.gpbat.admin.repository.DetailCommandeRepository;
import fr.kearis.gpbat.admin.service.DetailCommandeService;
import fr.kearis.gpbat.admin.repository.search.DetailCommandeSearchRepository;
import fr.kearis.gpbat.admin.service.dto.DetailCommandeDTO;
import fr.kearis.gpbat.admin.service.mapper.DetailCommandeMapper;

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
 * Test class for the DetailCommandeResource REST controller.
 *
 * @see DetailCommandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class DetailCommandeResourceIntTest {

    private static final Float DEFAULT_QUANTITE = 1F;
    private static final Float UPDATED_QUANTITE = 2F;

    private static final String DEFAULT_LOCALISATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCALISATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE_COMMANDE = 1;
    private static final Integer UPDATED_ORDRE_COMMANDE = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Inject
    private DetailCommandeRepository detailCommandeRepository;

    @Inject
    private DetailCommandeMapper detailCommandeMapper;

    @Inject
    private DetailCommandeService detailCommandeService;

    @Inject
    private DetailCommandeSearchRepository detailCommandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDetailCommandeMockMvc;

    private DetailCommande detailCommande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DetailCommandeResource detailCommandeResource = new DetailCommandeResource();
        ReflectionTestUtils.setField(detailCommandeResource, "detailCommandeService", detailCommandeService);
        this.restDetailCommandeMockMvc = MockMvcBuilders.standaloneSetup(detailCommandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailCommande createEntity(EntityManager em) {
        DetailCommande detailCommande = new DetailCommande()
                .quantite(DEFAULT_QUANTITE)
                .localisation(DEFAULT_LOCALISATION)
                .ordreCommande(DEFAULT_ORDRE_COMMANDE)
                .libelle(DEFAULT_LIBELLE);
        return detailCommande;
    }

    @Before
    public void initTest() {
        detailCommandeSearchRepository.deleteAll();
        detailCommande = createEntity(em);
    }

    @Test
    @Transactional
    public void createDetailCommande() throws Exception {
        int databaseSizeBeforeCreate = detailCommandeRepository.findAll().size();

        // Create the DetailCommande
        DetailCommandeDTO detailCommandeDTO = detailCommandeMapper.detailCommandeToDetailCommandeDTO(detailCommande);

        restDetailCommandeMockMvc.perform(post("/api/detail-commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(detailCommandeDTO)))
                .andExpect(status().isCreated());

        // Validate the DetailCommande in the database
        List<DetailCommande> detailCommandes = detailCommandeRepository.findAll();
        assertThat(detailCommandes).hasSize(databaseSizeBeforeCreate + 1);
        DetailCommande testDetailCommande = detailCommandes.get(detailCommandes.size() - 1);
        assertThat(testDetailCommande.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testDetailCommande.getLocalisation()).isEqualTo(DEFAULT_LOCALISATION);
        assertThat(testDetailCommande.getOrdreCommande()).isEqualTo(DEFAULT_ORDRE_COMMANDE);
        assertThat(testDetailCommande.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the DetailCommande in ElasticSearch
        DetailCommande detailCommandeEs = detailCommandeSearchRepository.findOne(testDetailCommande.getId());
        assertThat(detailCommandeEs).isEqualToComparingFieldByField(testDetailCommande);
    }

    @Test
    @Transactional
    public void getAllDetailCommandes() throws Exception {
        // Initialize the database
        detailCommandeRepository.saveAndFlush(detailCommande);

        // Get all the detailCommandes
        restDetailCommandeMockMvc.perform(get("/api/detail-commandes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(detailCommande.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.doubleValue())))
                .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION.toString())))
                .andExpect(jsonPath("$.[*].ordreCommande").value(hasItem(DEFAULT_ORDRE_COMMANDE)))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getDetailCommande() throws Exception {
        // Initialize the database
        detailCommandeRepository.saveAndFlush(detailCommande);

        // Get the detailCommande
        restDetailCommandeMockMvc.perform(get("/api/detail-commandes/{id}", detailCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(detailCommande.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.doubleValue()))
            .andExpect(jsonPath("$.localisation").value(DEFAULT_LOCALISATION.toString()))
            .andExpect(jsonPath("$.ordreCommande").value(DEFAULT_ORDRE_COMMANDE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDetailCommande() throws Exception {
        // Get the detailCommande
        restDetailCommandeMockMvc.perform(get("/api/detail-commandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDetailCommande() throws Exception {
        // Initialize the database
        detailCommandeRepository.saveAndFlush(detailCommande);
        detailCommandeSearchRepository.save(detailCommande);
        int databaseSizeBeforeUpdate = detailCommandeRepository.findAll().size();

        // Update the detailCommande
        DetailCommande updatedDetailCommande = detailCommandeRepository.findOne(detailCommande.getId());
        updatedDetailCommande
                .quantite(UPDATED_QUANTITE)
                .localisation(UPDATED_LOCALISATION)
                .ordreCommande(UPDATED_ORDRE_COMMANDE)
                .libelle(UPDATED_LIBELLE);
        DetailCommandeDTO detailCommandeDTO = detailCommandeMapper.detailCommandeToDetailCommandeDTO(updatedDetailCommande);

        restDetailCommandeMockMvc.perform(put("/api/detail-commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(detailCommandeDTO)))
                .andExpect(status().isOk());

        // Validate the DetailCommande in the database
        List<DetailCommande> detailCommandes = detailCommandeRepository.findAll();
        assertThat(detailCommandes).hasSize(databaseSizeBeforeUpdate);
        DetailCommande testDetailCommande = detailCommandes.get(detailCommandes.size() - 1);
        assertThat(testDetailCommande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testDetailCommande.getLocalisation()).isEqualTo(UPDATED_LOCALISATION);
        assertThat(testDetailCommande.getOrdreCommande()).isEqualTo(UPDATED_ORDRE_COMMANDE);
        assertThat(testDetailCommande.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the DetailCommande in ElasticSearch
        DetailCommande detailCommandeEs = detailCommandeSearchRepository.findOne(testDetailCommande.getId());
        assertThat(detailCommandeEs).isEqualToComparingFieldByField(testDetailCommande);
    }

    @Test
    @Transactional
    public void deleteDetailCommande() throws Exception {
        // Initialize the database
        detailCommandeRepository.saveAndFlush(detailCommande);
        detailCommandeSearchRepository.save(detailCommande);
        int databaseSizeBeforeDelete = detailCommandeRepository.findAll().size();

        // Get the detailCommande
        restDetailCommandeMockMvc.perform(delete("/api/detail-commandes/{id}", detailCommande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean detailCommandeExistsInEs = detailCommandeSearchRepository.exists(detailCommande.getId());
        assertThat(detailCommandeExistsInEs).isFalse();

        // Validate the database is empty
        List<DetailCommande> detailCommandes = detailCommandeRepository.findAll();
        assertThat(detailCommandes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDetailCommande() throws Exception {
        // Initialize the database
        detailCommandeRepository.saveAndFlush(detailCommande);
        detailCommandeSearchRepository.save(detailCommande);

        // Search the detailCommande
        restDetailCommandeMockMvc.perform(get("/api/_search/detail-commandes?query=id:" + detailCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.doubleValue())))
            .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION.toString())))
            .andExpect(jsonPath("$.[*].ordreCommande").value(hasItem(DEFAULT_ORDRE_COMMANDE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
}
