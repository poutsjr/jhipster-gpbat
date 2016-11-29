package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.Commande;
import fr.kearis.gpbat.admin.repository.CommandeRepository;
import fr.kearis.gpbat.admin.service.CommandeService;
import fr.kearis.gpbat.admin.repository.search.CommandeSearchRepository;
import fr.kearis.gpbat.admin.service.dto.CommandeDTO;
import fr.kearis.gpbat.admin.service.mapper.CommandeMapper;

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

/**
 * Test class for the CommandeResource REST controller.
 *
 * @see CommandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class CommandeResourceIntTest {

    private static final LocalDate DEFAULT_DATE_EDITION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EDITION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RECEPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEPTION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REFERENCE_MARCHE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_MARCHE = "BBBBBBBBBB";

    private static final Long DEFAULT_MONTANT_HT = 1L;
    private static final Long UPDATED_MONTANT_HT = 2L;

    private static final Float DEFAULT_TYPE_TVA = 1F;
    private static final Float UPDATED_TYPE_TVA = 2F;

    private static final String DEFAULT_ETAT_FACTURATION = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_FACTURATION = "BBBBBBBBBB";

    @Inject
    private CommandeRepository commandeRepository;

    @Inject
    private CommandeMapper commandeMapper;

    @Inject
    private CommandeService commandeService;

    @Inject
    private CommandeSearchRepository commandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCommandeMockMvc;

    private Commande commande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommandeResource commandeResource = new CommandeResource();
        ReflectionTestUtils.setField(commandeResource, "commandeService", commandeService);
        this.restCommandeMockMvc = MockMvcBuilders.standaloneSetup(commandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
                .dateEdition(DEFAULT_DATE_EDITION)
                .dateReception(DEFAULT_DATE_RECEPTION)
                .referenceMarche(DEFAULT_REFERENCE_MARCHE)
                .montantHt(DEFAULT_MONTANT_HT)
                .typeTva(DEFAULT_TYPE_TVA)
                .etatFacturation(DEFAULT_ETAT_FACTURATION);
        return commande;
    }

    @Before
    public void initTest() {
        commandeSearchRepository.deleteAll();
        commande = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.commandeToCommandeDTO(commande);

        restCommandeMockMvc.perform(post("/api/commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
                .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandes.get(commandes.size() - 1);
        assertThat(testCommande.getDateEdition()).isEqualTo(DEFAULT_DATE_EDITION);
        assertThat(testCommande.getDateReception()).isEqualTo(DEFAULT_DATE_RECEPTION);
        assertThat(testCommande.getReferenceMarche()).isEqualTo(DEFAULT_REFERENCE_MARCHE);
        assertThat(testCommande.getMontantHt()).isEqualTo(DEFAULT_MONTANT_HT);
        assertThat(testCommande.getTypeTva()).isEqualTo(DEFAULT_TYPE_TVA);
        assertThat(testCommande.getEtatFacturation()).isEqualTo(DEFAULT_ETAT_FACTURATION);

        // Validate the Commande in ElasticSearch
        Commande commandeEs = commandeSearchRepository.findOne(testCommande.getId());
        assertThat(commandeEs).isEqualToComparingFieldByField(testCommande);
    }

    @Test
    @Transactional
    public void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandes
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateEdition").value(hasItem(DEFAULT_DATE_EDITION.toString())))
                .andExpect(jsonPath("$.[*].dateReception").value(hasItem(DEFAULT_DATE_RECEPTION.toString())))
                .andExpect(jsonPath("$.[*].referenceMarche").value(hasItem(DEFAULT_REFERENCE_MARCHE.toString())))
                .andExpect(jsonPath("$.[*].montantHt").value(hasItem(DEFAULT_MONTANT_HT.intValue())))
                .andExpect(jsonPath("$.[*].typeTva").value(hasItem(DEFAULT_TYPE_TVA.doubleValue())))
                .andExpect(jsonPath("$.[*].etatFacturation").value(hasItem(DEFAULT_ETAT_FACTURATION.toString())));
    }

    @Test
    @Transactional
    public void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.dateEdition").value(DEFAULT_DATE_EDITION.toString()))
            .andExpect(jsonPath("$.dateReception").value(DEFAULT_DATE_RECEPTION.toString()))
            .andExpect(jsonPath("$.referenceMarche").value(DEFAULT_REFERENCE_MARCHE.toString()))
            .andExpect(jsonPath("$.montantHt").value(DEFAULT_MONTANT_HT.intValue()))
            .andExpect(jsonPath("$.typeTva").value(DEFAULT_TYPE_TVA.doubleValue()))
            .andExpect(jsonPath("$.etatFacturation").value(DEFAULT_ETAT_FACTURATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        commandeSearchRepository.save(commande);
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findOne(commande.getId());
        updatedCommande
                .dateEdition(UPDATED_DATE_EDITION)
                .dateReception(UPDATED_DATE_RECEPTION)
                .referenceMarche(UPDATED_REFERENCE_MARCHE)
                .montantHt(UPDATED_MONTANT_HT)
                .typeTva(UPDATED_TYPE_TVA)
                .etatFacturation(UPDATED_ETAT_FACTURATION);
        CommandeDTO commandeDTO = commandeMapper.commandeToCommandeDTO(updatedCommande);

        restCommandeMockMvc.perform(put("/api/commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
                .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandes.get(commandes.size() - 1);
        assertThat(testCommande.getDateEdition()).isEqualTo(UPDATED_DATE_EDITION);
        assertThat(testCommande.getDateReception()).isEqualTo(UPDATED_DATE_RECEPTION);
        assertThat(testCommande.getReferenceMarche()).isEqualTo(UPDATED_REFERENCE_MARCHE);
        assertThat(testCommande.getMontantHt()).isEqualTo(UPDATED_MONTANT_HT);
        assertThat(testCommande.getTypeTva()).isEqualTo(UPDATED_TYPE_TVA);
        assertThat(testCommande.getEtatFacturation()).isEqualTo(UPDATED_ETAT_FACTURATION);

        // Validate the Commande in ElasticSearch
        Commande commandeEs = commandeSearchRepository.findOne(testCommande.getId());
        assertThat(commandeEs).isEqualToComparingFieldByField(testCommande);
    }

    @Test
    @Transactional
    public void deleteCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        commandeSearchRepository.save(commande);
        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Get the commande
        restCommandeMockMvc.perform(delete("/api/commandes/{id}", commande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean commandeExistsInEs = commandeSearchRepository.exists(commande.getId());
        assertThat(commandeExistsInEs).isFalse();

        // Validate the database is empty
        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        commandeSearchRepository.save(commande);

        // Search the commande
        restCommandeMockMvc.perform(get("/api/_search/commandes?query=id:" + commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateEdition").value(hasItem(DEFAULT_DATE_EDITION.toString())))
            .andExpect(jsonPath("$.[*].dateReception").value(hasItem(DEFAULT_DATE_RECEPTION.toString())))
            .andExpect(jsonPath("$.[*].referenceMarche").value(hasItem(DEFAULT_REFERENCE_MARCHE.toString())))
            .andExpect(jsonPath("$.[*].montantHt").value(hasItem(DEFAULT_MONTANT_HT.intValue())))
            .andExpect(jsonPath("$.[*].typeTva").value(hasItem(DEFAULT_TYPE_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].etatFacturation").value(hasItem(DEFAULT_ETAT_FACTURATION.toString())));
    }
}
