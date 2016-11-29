package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.ReceptionChantier;
import fr.kearis.gpbat.admin.repository.ReceptionChantierRepository;
import fr.kearis.gpbat.admin.service.ReceptionChantierService;
import fr.kearis.gpbat.admin.repository.search.ReceptionChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ReceptionChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.ReceptionChantierMapper;

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
 * Test class for the ReceptionChantierResource REST controller.
 *
 * @see ReceptionChantierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class ReceptionChantierResourceIntTest {

    private static final String DEFAULT_DATE_RECEPTION = "AAAAAAAAAA";
    private static final String UPDATED_DATE_RECEPTION = "BBBBBBBBBB";

    @Inject
    private ReceptionChantierRepository receptionChantierRepository;

    @Inject
    private ReceptionChantierMapper receptionChantierMapper;

    @Inject
    private ReceptionChantierService receptionChantierService;

    @Inject
    private ReceptionChantierSearchRepository receptionChantierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restReceptionChantierMockMvc;

    private ReceptionChantier receptionChantier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReceptionChantierResource receptionChantierResource = new ReceptionChantierResource();
        ReflectionTestUtils.setField(receptionChantierResource, "receptionChantierService", receptionChantierService);
        this.restReceptionChantierMockMvc = MockMvcBuilders.standaloneSetup(receptionChantierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceptionChantier createEntity(EntityManager em) {
        ReceptionChantier receptionChantier = new ReceptionChantier()
                .dateReception(DEFAULT_DATE_RECEPTION);
        return receptionChantier;
    }

    @Before
    public void initTest() {
        receptionChantierSearchRepository.deleteAll();
        receptionChantier = createEntity(em);
    }

    @Test
    @Transactional
    public void createReceptionChantier() throws Exception {
        int databaseSizeBeforeCreate = receptionChantierRepository.findAll().size();

        // Create the ReceptionChantier
        ReceptionChantierDTO receptionChantierDTO = receptionChantierMapper.receptionChantierToReceptionChantierDTO(receptionChantier);

        restReceptionChantierMockMvc.perform(post("/api/reception-chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(receptionChantierDTO)))
                .andExpect(status().isCreated());

        // Validate the ReceptionChantier in the database
        List<ReceptionChantier> receptionChantiers = receptionChantierRepository.findAll();
        assertThat(receptionChantiers).hasSize(databaseSizeBeforeCreate + 1);
        ReceptionChantier testReceptionChantier = receptionChantiers.get(receptionChantiers.size() - 1);
        assertThat(testReceptionChantier.getDateReception()).isEqualTo(DEFAULT_DATE_RECEPTION);

        // Validate the ReceptionChantier in ElasticSearch
        ReceptionChantier receptionChantierEs = receptionChantierSearchRepository.findOne(testReceptionChantier.getId());
        assertThat(receptionChantierEs).isEqualToComparingFieldByField(testReceptionChantier);
    }

    @Test
    @Transactional
    public void getAllReceptionChantiers() throws Exception {
        // Initialize the database
        receptionChantierRepository.saveAndFlush(receptionChantier);

        // Get all the receptionChantiers
        restReceptionChantierMockMvc.perform(get("/api/reception-chantiers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(receptionChantier.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateReception").value(hasItem(DEFAULT_DATE_RECEPTION.toString())));
    }

    @Test
    @Transactional
    public void getReceptionChantier() throws Exception {
        // Initialize the database
        receptionChantierRepository.saveAndFlush(receptionChantier);

        // Get the receptionChantier
        restReceptionChantierMockMvc.perform(get("/api/reception-chantiers/{id}", receptionChantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(receptionChantier.getId().intValue()))
            .andExpect(jsonPath("$.dateReception").value(DEFAULT_DATE_RECEPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReceptionChantier() throws Exception {
        // Get the receptionChantier
        restReceptionChantierMockMvc.perform(get("/api/reception-chantiers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReceptionChantier() throws Exception {
        // Initialize the database
        receptionChantierRepository.saveAndFlush(receptionChantier);
        receptionChantierSearchRepository.save(receptionChantier);
        int databaseSizeBeforeUpdate = receptionChantierRepository.findAll().size();

        // Update the receptionChantier
        ReceptionChantier updatedReceptionChantier = receptionChantierRepository.findOne(receptionChantier.getId());
        updatedReceptionChantier
                .dateReception(UPDATED_DATE_RECEPTION);
        ReceptionChantierDTO receptionChantierDTO = receptionChantierMapper.receptionChantierToReceptionChantierDTO(updatedReceptionChantier);

        restReceptionChantierMockMvc.perform(put("/api/reception-chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(receptionChantierDTO)))
                .andExpect(status().isOk());

        // Validate the ReceptionChantier in the database
        List<ReceptionChantier> receptionChantiers = receptionChantierRepository.findAll();
        assertThat(receptionChantiers).hasSize(databaseSizeBeforeUpdate);
        ReceptionChantier testReceptionChantier = receptionChantiers.get(receptionChantiers.size() - 1);
        assertThat(testReceptionChantier.getDateReception()).isEqualTo(UPDATED_DATE_RECEPTION);

        // Validate the ReceptionChantier in ElasticSearch
        ReceptionChantier receptionChantierEs = receptionChantierSearchRepository.findOne(testReceptionChantier.getId());
        assertThat(receptionChantierEs).isEqualToComparingFieldByField(testReceptionChantier);
    }

    @Test
    @Transactional
    public void deleteReceptionChantier() throws Exception {
        // Initialize the database
        receptionChantierRepository.saveAndFlush(receptionChantier);
        receptionChantierSearchRepository.save(receptionChantier);
        int databaseSizeBeforeDelete = receptionChantierRepository.findAll().size();

        // Get the receptionChantier
        restReceptionChantierMockMvc.perform(delete("/api/reception-chantiers/{id}", receptionChantier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean receptionChantierExistsInEs = receptionChantierSearchRepository.exists(receptionChantier.getId());
        assertThat(receptionChantierExistsInEs).isFalse();

        // Validate the database is empty
        List<ReceptionChantier> receptionChantiers = receptionChantierRepository.findAll();
        assertThat(receptionChantiers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReceptionChantier() throws Exception {
        // Initialize the database
        receptionChantierRepository.saveAndFlush(receptionChantier);
        receptionChantierSearchRepository.save(receptionChantier);

        // Search the receptionChantier
        restReceptionChantierMockMvc.perform(get("/api/_search/reception-chantiers?query=id:" + receptionChantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receptionChantier.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateReception").value(hasItem(DEFAULT_DATE_RECEPTION.toString())));
    }
}
