package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.CorpsEtat;
import fr.kearis.gpbat.admin.repository.CorpsEtatRepository;
import fr.kearis.gpbat.admin.service.CorpsEtatService;
import fr.kearis.gpbat.admin.repository.search.CorpsEtatSearchRepository;
import fr.kearis.gpbat.admin.service.dto.CorpsEtatDTO;
import fr.kearis.gpbat.admin.service.mapper.CorpsEtatMapper;

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
 * Test class for the CorpsEtatResource REST controller.
 *
 * @see CorpsEtatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class CorpsEtatResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Inject
    private CorpsEtatRepository corpsEtatRepository;

    @Inject
    private CorpsEtatMapper corpsEtatMapper;

    @Inject
    private CorpsEtatService corpsEtatService;

    @Inject
    private CorpsEtatSearchRepository corpsEtatSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCorpsEtatMockMvc;

    private CorpsEtat corpsEtat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CorpsEtatResource corpsEtatResource = new CorpsEtatResource();
        ReflectionTestUtils.setField(corpsEtatResource, "corpsEtatService", corpsEtatService);
        this.restCorpsEtatMockMvc = MockMvcBuilders.standaloneSetup(corpsEtatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorpsEtat createEntity(EntityManager em) {
        CorpsEtat corpsEtat = new CorpsEtat()
                .nom(DEFAULT_NOM);
        return corpsEtat;
    }

    @Before
    public void initTest() {
        corpsEtatSearchRepository.deleteAll();
        corpsEtat = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorpsEtat() throws Exception {
        int databaseSizeBeforeCreate = corpsEtatRepository.findAll().size();

        // Create the CorpsEtat
        CorpsEtatDTO corpsEtatDTO = corpsEtatMapper.corpsEtatToCorpsEtatDTO(corpsEtat);

        restCorpsEtatMockMvc.perform(post("/api/corps-etats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(corpsEtatDTO)))
                .andExpect(status().isCreated());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtats = corpsEtatRepository.findAll();
        assertThat(corpsEtats).hasSize(databaseSizeBeforeCreate + 1);
        CorpsEtat testCorpsEtat = corpsEtats.get(corpsEtats.size() - 1);
        assertThat(testCorpsEtat.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the CorpsEtat in ElasticSearch
        CorpsEtat corpsEtatEs = corpsEtatSearchRepository.findOne(testCorpsEtat.getId());
        assertThat(corpsEtatEs).isEqualToComparingFieldByField(testCorpsEtat);
    }

    @Test
    @Transactional
    public void getAllCorpsEtats() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);

        // Get all the corpsEtats
        restCorpsEtatMockMvc.perform(get("/api/corps-etats?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(corpsEtat.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getCorpsEtat() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);

        // Get the corpsEtat
        restCorpsEtatMockMvc.perform(get("/api/corps-etats/{id}", corpsEtat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(corpsEtat.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCorpsEtat() throws Exception {
        // Get the corpsEtat
        restCorpsEtatMockMvc.perform(get("/api/corps-etats/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorpsEtat() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);
        corpsEtatSearchRepository.save(corpsEtat);
        int databaseSizeBeforeUpdate = corpsEtatRepository.findAll().size();

        // Update the corpsEtat
        CorpsEtat updatedCorpsEtat = corpsEtatRepository.findOne(corpsEtat.getId());
        updatedCorpsEtat
                .nom(UPDATED_NOM);
        CorpsEtatDTO corpsEtatDTO = corpsEtatMapper.corpsEtatToCorpsEtatDTO(updatedCorpsEtat);

        restCorpsEtatMockMvc.perform(put("/api/corps-etats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(corpsEtatDTO)))
                .andExpect(status().isOk());

        // Validate the CorpsEtat in the database
        List<CorpsEtat> corpsEtats = corpsEtatRepository.findAll();
        assertThat(corpsEtats).hasSize(databaseSizeBeforeUpdate);
        CorpsEtat testCorpsEtat = corpsEtats.get(corpsEtats.size() - 1);
        assertThat(testCorpsEtat.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the CorpsEtat in ElasticSearch
        CorpsEtat corpsEtatEs = corpsEtatSearchRepository.findOne(testCorpsEtat.getId());
        assertThat(corpsEtatEs).isEqualToComparingFieldByField(testCorpsEtat);
    }

    @Test
    @Transactional
    public void deleteCorpsEtat() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);
        corpsEtatSearchRepository.save(corpsEtat);
        int databaseSizeBeforeDelete = corpsEtatRepository.findAll().size();

        // Get the corpsEtat
        restCorpsEtatMockMvc.perform(delete("/api/corps-etats/{id}", corpsEtat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean corpsEtatExistsInEs = corpsEtatSearchRepository.exists(corpsEtat.getId());
        assertThat(corpsEtatExistsInEs).isFalse();

        // Validate the database is empty
        List<CorpsEtat> corpsEtats = corpsEtatRepository.findAll();
        assertThat(corpsEtats).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCorpsEtat() throws Exception {
        // Initialize the database
        corpsEtatRepository.saveAndFlush(corpsEtat);
        corpsEtatSearchRepository.save(corpsEtat);

        // Search the corpsEtat
        restCorpsEtatMockMvc.perform(get("/api/_search/corps-etats?query=id:" + corpsEtat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corpsEtat.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }
}
