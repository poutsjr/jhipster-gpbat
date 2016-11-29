package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.Chantier;
import fr.kearis.gpbat.admin.repository.ChantierRepository;
import fr.kearis.gpbat.admin.service.ChantierService;
import fr.kearis.gpbat.admin.repository.search.ChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.ChantierMapper;

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
 * Test class for the ChantierResource REST controller.
 *
 * @see ChantierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class ChantierResourceIntTest {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_DEMANDE_TRAVAUX = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMANDE_TRAVAUX = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ChantierRepository chantierRepository;

    @Inject
    private ChantierMapper chantierMapper;

    @Inject
    private ChantierService chantierService;

    @Inject
    private ChantierSearchRepository chantierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChantierMockMvc;

    private Chantier chantier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChantierResource chantierResource = new ChantierResource();
        ReflectionTestUtils.setField(chantierResource, "chantierService", chantierService);
        this.restChantierMockMvc = MockMvcBuilders.standaloneSetup(chantierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chantier createEntity(EntityManager em) {
        Chantier chantier = new Chantier()
                .reference(DEFAULT_REFERENCE)
                .adresse(DEFAULT_ADRESSE)
                .dateDebut(DEFAULT_DATE_DEBUT)
                .dateDemandeTravaux(DEFAULT_DATE_DEMANDE_TRAVAUX);
        return chantier;
    }

    @Before
    public void initTest() {
        chantierSearchRepository.deleteAll();
        chantier = createEntity(em);
    }

    @Test
    @Transactional
    public void createChantier() throws Exception {
        int databaseSizeBeforeCreate = chantierRepository.findAll().size();

        // Create the Chantier
        ChantierDTO chantierDTO = chantierMapper.chantierToChantierDTO(chantier);

        restChantierMockMvc.perform(post("/api/chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
                .andExpect(status().isCreated());

        // Validate the Chantier in the database
        List<Chantier> chantiers = chantierRepository.findAll();
        assertThat(chantiers).hasSize(databaseSizeBeforeCreate + 1);
        Chantier testChantier = chantiers.get(chantiers.size() - 1);
        assertThat(testChantier.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testChantier.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testChantier.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testChantier.getDateDemandeTravaux()).isEqualTo(DEFAULT_DATE_DEMANDE_TRAVAUX);

        // Validate the Chantier in ElasticSearch
        Chantier chantierEs = chantierSearchRepository.findOne(testChantier.getId());
        assertThat(chantierEs).isEqualToComparingFieldByField(testChantier);
    }

    @Test
    @Transactional
    public void getAllChantiers() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);

        // Get all the chantiers
        restChantierMockMvc.perform(get("/api/chantiers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(chantier.getId().intValue())))
                .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateDemandeTravaux").value(hasItem(DEFAULT_DATE_DEMANDE_TRAVAUX.toString())));
    }

    @Test
    @Transactional
    public void getChantier() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);

        // Get the chantier
        restChantierMockMvc.perform(get("/api/chantiers/{id}", chantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chantier.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateDemandeTravaux").value(DEFAULT_DATE_DEMANDE_TRAVAUX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChantier() throws Exception {
        // Get the chantier
        restChantierMockMvc.perform(get("/api/chantiers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChantier() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);
        chantierSearchRepository.save(chantier);
        int databaseSizeBeforeUpdate = chantierRepository.findAll().size();

        // Update the chantier
        Chantier updatedChantier = chantierRepository.findOne(chantier.getId());
        updatedChantier
                .reference(UPDATED_REFERENCE)
                .adresse(UPDATED_ADRESSE)
                .dateDebut(UPDATED_DATE_DEBUT)
                .dateDemandeTravaux(UPDATED_DATE_DEMANDE_TRAVAUX);
        ChantierDTO chantierDTO = chantierMapper.chantierToChantierDTO(updatedChantier);

        restChantierMockMvc.perform(put("/api/chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
                .andExpect(status().isOk());

        // Validate the Chantier in the database
        List<Chantier> chantiers = chantierRepository.findAll();
        assertThat(chantiers).hasSize(databaseSizeBeforeUpdate);
        Chantier testChantier = chantiers.get(chantiers.size() - 1);
        assertThat(testChantier.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testChantier.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChantier.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testChantier.getDateDemandeTravaux()).isEqualTo(UPDATED_DATE_DEMANDE_TRAVAUX);

        // Validate the Chantier in ElasticSearch
        Chantier chantierEs = chantierSearchRepository.findOne(testChantier.getId());
        assertThat(chantierEs).isEqualToComparingFieldByField(testChantier);
    }

    @Test
    @Transactional
    public void deleteChantier() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);
        chantierSearchRepository.save(chantier);
        int databaseSizeBeforeDelete = chantierRepository.findAll().size();

        // Get the chantier
        restChantierMockMvc.perform(delete("/api/chantiers/{id}", chantier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean chantierExistsInEs = chantierSearchRepository.exists(chantier.getId());
        assertThat(chantierExistsInEs).isFalse();

        // Validate the database is empty
        List<Chantier> chantiers = chantierRepository.findAll();
        assertThat(chantiers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChantier() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);
        chantierSearchRepository.save(chantier);

        // Search the chantier
        restChantierMockMvc.perform(get("/api/_search/chantiers?query=id:" + chantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chantier.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateDemandeTravaux").value(hasItem(DEFAULT_DATE_DEMANDE_TRAVAUX.toString())));
    }
}
