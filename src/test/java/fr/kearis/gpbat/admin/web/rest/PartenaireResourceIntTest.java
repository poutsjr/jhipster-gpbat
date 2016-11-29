package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.Partenaire;
import fr.kearis.gpbat.admin.repository.PartenaireRepository;
import fr.kearis.gpbat.admin.service.PartenaireService;
import fr.kearis.gpbat.admin.repository.search.PartenaireSearchRepository;
import fr.kearis.gpbat.admin.service.dto.PartenaireDTO;
import fr.kearis.gpbat.admin.service.mapper.PartenaireMapper;

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
 * Test class for the PartenaireResource REST controller.
 *
 * @see PartenaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class PartenaireResourceIntTest {

    private static final String DEFAULT_RAISON_SOCIALE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIALE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    @Inject
    private PartenaireRepository partenaireRepository;

    @Inject
    private PartenaireMapper partenaireMapper;

    @Inject
    private PartenaireService partenaireService;

    @Inject
    private PartenaireSearchRepository partenaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPartenaireMockMvc;

    private Partenaire partenaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PartenaireResource partenaireResource = new PartenaireResource();
        ReflectionTestUtils.setField(partenaireResource, "partenaireService", partenaireService);
        this.restPartenaireMockMvc = MockMvcBuilders.standaloneSetup(partenaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partenaire createEntity(EntityManager em) {
        Partenaire partenaire = new Partenaire()
                .raisonSociale(DEFAULT_RAISON_SOCIALE)
                .responsable(DEFAULT_RESPONSABLE)
                .contact(DEFAULT_CONTACT)
                .adresse(DEFAULT_ADRESSE);
        return partenaire;
    }

    @Before
    public void initTest() {
        partenaireSearchRepository.deleteAll();
        partenaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartenaire() throws Exception {
        int databaseSizeBeforeCreate = partenaireRepository.findAll().size();

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.partenaireToPartenaireDTO(partenaire);

        restPartenaireMockMvc.perform(post("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partenaireDTO)))
                .andExpect(status().isCreated());

        // Validate the Partenaire in the database
        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeCreate + 1);
        Partenaire testPartenaire = partenaires.get(partenaires.size() - 1);
        assertThat(testPartenaire.getRaisonSociale()).isEqualTo(DEFAULT_RAISON_SOCIALE);
        assertThat(testPartenaire.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testPartenaire.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testPartenaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);

        // Validate the Partenaire in ElasticSearch
        Partenaire partenaireEs = partenaireSearchRepository.findOne(testPartenaire.getId());
        assertThat(partenaireEs).isEqualToComparingFieldByField(testPartenaire);
    }

    @Test
    @Transactional
    public void getAllPartenaires() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaires
        restPartenaireMockMvc.perform(get("/api/partenaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE.toString())))
                .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE.toString())))
                .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())));
    }

    @Test
    @Transactional
    public void getPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get the partenaire
        restPartenaireMockMvc.perform(get("/api/partenaires/{id}", partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(partenaire.getId().intValue()))
            .andExpect(jsonPath("$.raisonSociale").value(DEFAULT_RAISON_SOCIALE.toString()))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPartenaire() throws Exception {
        // Get the partenaire
        restPartenaireMockMvc.perform(get("/api/partenaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Update the partenaire
        Partenaire updatedPartenaire = partenaireRepository.findOne(partenaire.getId());
        updatedPartenaire
                .raisonSociale(UPDATED_RAISON_SOCIALE)
                .responsable(UPDATED_RESPONSABLE)
                .contact(UPDATED_CONTACT)
                .adresse(UPDATED_ADRESSE);
        PartenaireDTO partenaireDTO = partenaireMapper.partenaireToPartenaireDTO(updatedPartenaire);

        restPartenaireMockMvc.perform(put("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partenaireDTO)))
                .andExpect(status().isOk());

        // Validate the Partenaire in the database
        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeUpdate);
        Partenaire testPartenaire = partenaires.get(partenaires.size() - 1);
        assertThat(testPartenaire.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testPartenaire.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testPartenaire.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testPartenaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);

        // Validate the Partenaire in ElasticSearch
        Partenaire partenaireEs = partenaireSearchRepository.findOne(testPartenaire.getId());
        assertThat(partenaireEs).isEqualToComparingFieldByField(testPartenaire);
    }

    @Test
    @Transactional
    public void deletePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);
        int databaseSizeBeforeDelete = partenaireRepository.findAll().size();

        // Get the partenaire
        restPartenaireMockMvc.perform(delete("/api/partenaires/{id}", partenaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean partenaireExistsInEs = partenaireSearchRepository.exists(partenaire.getId());
        assertThat(partenaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);

        // Search the partenaire
        restPartenaireMockMvc.perform(get("/api/_search/partenaires?query=id:" + partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())));
    }
}
