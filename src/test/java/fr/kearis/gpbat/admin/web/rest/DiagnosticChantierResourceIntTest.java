package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.DiagnosticChantier;
import fr.kearis.gpbat.admin.repository.DiagnosticChantierRepository;
import fr.kearis.gpbat.admin.service.DiagnosticChantierService;
import fr.kearis.gpbat.admin.repository.search.DiagnosticChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.DiagnosticChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.DiagnosticChantierMapper;

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

import fr.kearis.gpbat.admin.domain.enumeration.TypeDiagnostic;
/**
 * Test class for the DiagnosticChantierResource REST controller.
 *
 * @see DiagnosticChantierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class DiagnosticChantierResourceIntTest {

    private static final TypeDiagnostic DEFAULT_TYPE_DIAGNOSTIC = TypeDiagnostic.AMIANTE;
    private static final TypeDiagnostic UPDATED_TYPE_DIAGNOSTIC = TypeDiagnostic.ELECTRICITE;

    private static final LocalDate DEFAULT_DATE_DIAGNOSTIC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DIAGNOSTIC = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REFERENCE_DIAGNOSTIC = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_DIAGNOSTIC = "BBBBBBBBBB";

    @Inject
    private DiagnosticChantierRepository diagnosticChantierRepository;

    @Inject
    private DiagnosticChantierMapper diagnosticChantierMapper;

    @Inject
    private DiagnosticChantierService diagnosticChantierService;

    @Inject
    private DiagnosticChantierSearchRepository diagnosticChantierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDiagnosticChantierMockMvc;

    private DiagnosticChantier diagnosticChantier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiagnosticChantierResource diagnosticChantierResource = new DiagnosticChantierResource();
        ReflectionTestUtils.setField(diagnosticChantierResource, "diagnosticChantierService", diagnosticChantierService);
        this.restDiagnosticChantierMockMvc = MockMvcBuilders.standaloneSetup(diagnosticChantierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiagnosticChantier createEntity(EntityManager em) {
        DiagnosticChantier diagnosticChantier = new DiagnosticChantier()
                .typeDiagnostic(DEFAULT_TYPE_DIAGNOSTIC)
                .dateDiagnostic(DEFAULT_DATE_DIAGNOSTIC)
                .referenceDiagnostic(DEFAULT_REFERENCE_DIAGNOSTIC);
        return diagnosticChantier;
    }

    @Before
    public void initTest() {
        diagnosticChantierSearchRepository.deleteAll();
        diagnosticChantier = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiagnosticChantier() throws Exception {
        int databaseSizeBeforeCreate = diagnosticChantierRepository.findAll().size();

        // Create the DiagnosticChantier
        DiagnosticChantierDTO diagnosticChantierDTO = diagnosticChantierMapper.diagnosticChantierToDiagnosticChantierDTO(diagnosticChantier);

        restDiagnosticChantierMockMvc.perform(post("/api/diagnostic-chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diagnosticChantierDTO)))
                .andExpect(status().isCreated());

        // Validate the DiagnosticChantier in the database
        List<DiagnosticChantier> diagnosticChantiers = diagnosticChantierRepository.findAll();
        assertThat(diagnosticChantiers).hasSize(databaseSizeBeforeCreate + 1);
        DiagnosticChantier testDiagnosticChantier = diagnosticChantiers.get(diagnosticChantiers.size() - 1);
        assertThat(testDiagnosticChantier.getTypeDiagnostic()).isEqualTo(DEFAULT_TYPE_DIAGNOSTIC);
        assertThat(testDiagnosticChantier.getDateDiagnostic()).isEqualTo(DEFAULT_DATE_DIAGNOSTIC);
        assertThat(testDiagnosticChantier.getReferenceDiagnostic()).isEqualTo(DEFAULT_REFERENCE_DIAGNOSTIC);

        // Validate the DiagnosticChantier in ElasticSearch
        DiagnosticChantier diagnosticChantierEs = diagnosticChantierSearchRepository.findOne(testDiagnosticChantier.getId());
        assertThat(diagnosticChantierEs).isEqualToComparingFieldByField(testDiagnosticChantier);
    }

    @Test
    @Transactional
    public void getAllDiagnosticChantiers() throws Exception {
        // Initialize the database
        diagnosticChantierRepository.saveAndFlush(diagnosticChantier);

        // Get all the diagnosticChantiers
        restDiagnosticChantierMockMvc.perform(get("/api/diagnostic-chantiers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(diagnosticChantier.getId().intValue())))
                .andExpect(jsonPath("$.[*].typeDiagnostic").value(hasItem(DEFAULT_TYPE_DIAGNOSTIC.toString())))
                .andExpect(jsonPath("$.[*].dateDiagnostic").value(hasItem(DEFAULT_DATE_DIAGNOSTIC.toString())))
                .andExpect(jsonPath("$.[*].referenceDiagnostic").value(hasItem(DEFAULT_REFERENCE_DIAGNOSTIC.toString())));
    }

    @Test
    @Transactional
    public void getDiagnosticChantier() throws Exception {
        // Initialize the database
        diagnosticChantierRepository.saveAndFlush(diagnosticChantier);

        // Get the diagnosticChantier
        restDiagnosticChantierMockMvc.perform(get("/api/diagnostic-chantiers/{id}", diagnosticChantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diagnosticChantier.getId().intValue()))
            .andExpect(jsonPath("$.typeDiagnostic").value(DEFAULT_TYPE_DIAGNOSTIC.toString()))
            .andExpect(jsonPath("$.dateDiagnostic").value(DEFAULT_DATE_DIAGNOSTIC.toString()))
            .andExpect(jsonPath("$.referenceDiagnostic").value(DEFAULT_REFERENCE_DIAGNOSTIC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiagnosticChantier() throws Exception {
        // Get the diagnosticChantier
        restDiagnosticChantierMockMvc.perform(get("/api/diagnostic-chantiers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiagnosticChantier() throws Exception {
        // Initialize the database
        diagnosticChantierRepository.saveAndFlush(diagnosticChantier);
        diagnosticChantierSearchRepository.save(diagnosticChantier);
        int databaseSizeBeforeUpdate = diagnosticChantierRepository.findAll().size();

        // Update the diagnosticChantier
        DiagnosticChantier updatedDiagnosticChantier = diagnosticChantierRepository.findOne(diagnosticChantier.getId());
        updatedDiagnosticChantier
                .typeDiagnostic(UPDATED_TYPE_DIAGNOSTIC)
                .dateDiagnostic(UPDATED_DATE_DIAGNOSTIC)
                .referenceDiagnostic(UPDATED_REFERENCE_DIAGNOSTIC);
        DiagnosticChantierDTO diagnosticChantierDTO = diagnosticChantierMapper.diagnosticChantierToDiagnosticChantierDTO(updatedDiagnosticChantier);

        restDiagnosticChantierMockMvc.perform(put("/api/diagnostic-chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diagnosticChantierDTO)))
                .andExpect(status().isOk());

        // Validate the DiagnosticChantier in the database
        List<DiagnosticChantier> diagnosticChantiers = diagnosticChantierRepository.findAll();
        assertThat(diagnosticChantiers).hasSize(databaseSizeBeforeUpdate);
        DiagnosticChantier testDiagnosticChantier = diagnosticChantiers.get(diagnosticChantiers.size() - 1);
        assertThat(testDiagnosticChantier.getTypeDiagnostic()).isEqualTo(UPDATED_TYPE_DIAGNOSTIC);
        assertThat(testDiagnosticChantier.getDateDiagnostic()).isEqualTo(UPDATED_DATE_DIAGNOSTIC);
        assertThat(testDiagnosticChantier.getReferenceDiagnostic()).isEqualTo(UPDATED_REFERENCE_DIAGNOSTIC);

        // Validate the DiagnosticChantier in ElasticSearch
        DiagnosticChantier diagnosticChantierEs = diagnosticChantierSearchRepository.findOne(testDiagnosticChantier.getId());
        assertThat(diagnosticChantierEs).isEqualToComparingFieldByField(testDiagnosticChantier);
    }

    @Test
    @Transactional
    public void deleteDiagnosticChantier() throws Exception {
        // Initialize the database
        diagnosticChantierRepository.saveAndFlush(diagnosticChantier);
        diagnosticChantierSearchRepository.save(diagnosticChantier);
        int databaseSizeBeforeDelete = diagnosticChantierRepository.findAll().size();

        // Get the diagnosticChantier
        restDiagnosticChantierMockMvc.perform(delete("/api/diagnostic-chantiers/{id}", diagnosticChantier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean diagnosticChantierExistsInEs = diagnosticChantierSearchRepository.exists(diagnosticChantier.getId());
        assertThat(diagnosticChantierExistsInEs).isFalse();

        // Validate the database is empty
        List<DiagnosticChantier> diagnosticChantiers = diagnosticChantierRepository.findAll();
        assertThat(diagnosticChantiers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDiagnosticChantier() throws Exception {
        // Initialize the database
        diagnosticChantierRepository.saveAndFlush(diagnosticChantier);
        diagnosticChantierSearchRepository.save(diagnosticChantier);

        // Search the diagnosticChantier
        restDiagnosticChantierMockMvc.perform(get("/api/_search/diagnostic-chantiers?query=id:" + diagnosticChantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diagnosticChantier.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeDiagnostic").value(hasItem(DEFAULT_TYPE_DIAGNOSTIC.toString())))
            .andExpect(jsonPath("$.[*].dateDiagnostic").value(hasItem(DEFAULT_DATE_DIAGNOSTIC.toString())))
            .andExpect(jsonPath("$.[*].referenceDiagnostic").value(hasItem(DEFAULT_REFERENCE_DIAGNOSTIC.toString())));
    }
}
