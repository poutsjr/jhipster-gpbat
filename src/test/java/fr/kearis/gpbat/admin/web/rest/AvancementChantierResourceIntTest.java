package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.AvancementChantier;
import fr.kearis.gpbat.admin.repository.AvancementChantierRepository;
import fr.kearis.gpbat.admin.service.AvancementChantierService;
import fr.kearis.gpbat.admin.repository.search.AvancementChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.AvancementChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.AvancementChantierMapper;

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
 * Test class for the AvancementChantierResource REST controller.
 *
 * @see AvancementChantierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class AvancementChantierResourceIntTest {

    private static final Integer DEFAULT_AVANCEMENT_POURCENTAGE = 1;
    private static final Integer UPDATED_AVANCEMENT_POURCENTAGE = 2;

    private static final Float DEFAULT_AVANCEMENT_ETAT = 1F;
    private static final Float UPDATED_AVANCEMENT_ETAT = 2F;

    @Inject
    private AvancementChantierRepository avancementChantierRepository;

    @Inject
    private AvancementChantierMapper avancementChantierMapper;

    @Inject
    private AvancementChantierService avancementChantierService;

    @Inject
    private AvancementChantierSearchRepository avancementChantierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAvancementChantierMockMvc;

    private AvancementChantier avancementChantier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvancementChantierResource avancementChantierResource = new AvancementChantierResource();
        ReflectionTestUtils.setField(avancementChantierResource, "avancementChantierService", avancementChantierService);
        this.restAvancementChantierMockMvc = MockMvcBuilders.standaloneSetup(avancementChantierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvancementChantier createEntity(EntityManager em) {
        AvancementChantier avancementChantier = new AvancementChantier()
                .avancementPourcentage(DEFAULT_AVANCEMENT_POURCENTAGE)
                .avancementEtat(DEFAULT_AVANCEMENT_ETAT);
        return avancementChantier;
    }

    @Before
    public void initTest() {
        avancementChantierSearchRepository.deleteAll();
        avancementChantier = createEntity(em);
    }

    @Test
    @Transactional
    public void createAvancementChantier() throws Exception {
        int databaseSizeBeforeCreate = avancementChantierRepository.findAll().size();

        // Create the AvancementChantier
        AvancementChantierDTO avancementChantierDTO = avancementChantierMapper.avancementChantierToAvancementChantierDTO(avancementChantier);

        restAvancementChantierMockMvc.perform(post("/api/avancement-chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(avancementChantierDTO)))
                .andExpect(status().isCreated());

        // Validate the AvancementChantier in the database
        List<AvancementChantier> avancementChantiers = avancementChantierRepository.findAll();
        assertThat(avancementChantiers).hasSize(databaseSizeBeforeCreate + 1);
        AvancementChantier testAvancementChantier = avancementChantiers.get(avancementChantiers.size() - 1);
        assertThat(testAvancementChantier.getAvancementPourcentage()).isEqualTo(DEFAULT_AVANCEMENT_POURCENTAGE);
        assertThat(testAvancementChantier.getAvancementEtat()).isEqualTo(DEFAULT_AVANCEMENT_ETAT);

        // Validate the AvancementChantier in ElasticSearch
        AvancementChantier avancementChantierEs = avancementChantierSearchRepository.findOne(testAvancementChantier.getId());
        assertThat(avancementChantierEs).isEqualToComparingFieldByField(testAvancementChantier);
    }

    @Test
    @Transactional
    public void getAllAvancementChantiers() throws Exception {
        // Initialize the database
        avancementChantierRepository.saveAndFlush(avancementChantier);

        // Get all the avancementChantiers
        restAvancementChantierMockMvc.perform(get("/api/avancement-chantiers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(avancementChantier.getId().intValue())))
                .andExpect(jsonPath("$.[*].avancementPourcentage").value(hasItem(DEFAULT_AVANCEMENT_POURCENTAGE)))
                .andExpect(jsonPath("$.[*].avancementEtat").value(hasItem(DEFAULT_AVANCEMENT_ETAT.doubleValue())));
    }

    @Test
    @Transactional
    public void getAvancementChantier() throws Exception {
        // Initialize the database
        avancementChantierRepository.saveAndFlush(avancementChantier);

        // Get the avancementChantier
        restAvancementChantierMockMvc.perform(get("/api/avancement-chantiers/{id}", avancementChantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(avancementChantier.getId().intValue()))
            .andExpect(jsonPath("$.avancementPourcentage").value(DEFAULT_AVANCEMENT_POURCENTAGE))
            .andExpect(jsonPath("$.avancementEtat").value(DEFAULT_AVANCEMENT_ETAT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAvancementChantier() throws Exception {
        // Get the avancementChantier
        restAvancementChantierMockMvc.perform(get("/api/avancement-chantiers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvancementChantier() throws Exception {
        // Initialize the database
        avancementChantierRepository.saveAndFlush(avancementChantier);
        avancementChantierSearchRepository.save(avancementChantier);
        int databaseSizeBeforeUpdate = avancementChantierRepository.findAll().size();

        // Update the avancementChantier
        AvancementChantier updatedAvancementChantier = avancementChantierRepository.findOne(avancementChantier.getId());
        updatedAvancementChantier
                .avancementPourcentage(UPDATED_AVANCEMENT_POURCENTAGE)
                .avancementEtat(UPDATED_AVANCEMENT_ETAT);
        AvancementChantierDTO avancementChantierDTO = avancementChantierMapper.avancementChantierToAvancementChantierDTO(updatedAvancementChantier);

        restAvancementChantierMockMvc.perform(put("/api/avancement-chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(avancementChantierDTO)))
                .andExpect(status().isOk());

        // Validate the AvancementChantier in the database
        List<AvancementChantier> avancementChantiers = avancementChantierRepository.findAll();
        assertThat(avancementChantiers).hasSize(databaseSizeBeforeUpdate);
        AvancementChantier testAvancementChantier = avancementChantiers.get(avancementChantiers.size() - 1);
        assertThat(testAvancementChantier.getAvancementPourcentage()).isEqualTo(UPDATED_AVANCEMENT_POURCENTAGE);
        assertThat(testAvancementChantier.getAvancementEtat()).isEqualTo(UPDATED_AVANCEMENT_ETAT);

        // Validate the AvancementChantier in ElasticSearch
        AvancementChantier avancementChantierEs = avancementChantierSearchRepository.findOne(testAvancementChantier.getId());
        assertThat(avancementChantierEs).isEqualToComparingFieldByField(testAvancementChantier);
    }

    @Test
    @Transactional
    public void deleteAvancementChantier() throws Exception {
        // Initialize the database
        avancementChantierRepository.saveAndFlush(avancementChantier);
        avancementChantierSearchRepository.save(avancementChantier);
        int databaseSizeBeforeDelete = avancementChantierRepository.findAll().size();

        // Get the avancementChantier
        restAvancementChantierMockMvc.perform(delete("/api/avancement-chantiers/{id}", avancementChantier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean avancementChantierExistsInEs = avancementChantierSearchRepository.exists(avancementChantier.getId());
        assertThat(avancementChantierExistsInEs).isFalse();

        // Validate the database is empty
        List<AvancementChantier> avancementChantiers = avancementChantierRepository.findAll();
        assertThat(avancementChantiers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAvancementChantier() throws Exception {
        // Initialize the database
        avancementChantierRepository.saveAndFlush(avancementChantier);
        avancementChantierSearchRepository.save(avancementChantier);

        // Search the avancementChantier
        restAvancementChantierMockMvc.perform(get("/api/_search/avancement-chantiers?query=id:" + avancementChantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avancementChantier.getId().intValue())))
            .andExpect(jsonPath("$.[*].avancementPourcentage").value(hasItem(DEFAULT_AVANCEMENT_POURCENTAGE)))
            .andExpect(jsonPath("$.[*].avancementEtat").value(hasItem(DEFAULT_AVANCEMENT_ETAT.doubleValue())));
    }
}
