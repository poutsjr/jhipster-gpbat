package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.Utilisateur;
import fr.kearis.gpbat.admin.repository.UtilisateurRepository;
import fr.kearis.gpbat.admin.service.UtilisateurService;
import fr.kearis.gpbat.admin.repository.search.UtilisateurSearchRepository;
import fr.kearis.gpbat.admin.service.dto.UtilisateurDTO;
import fr.kearis.gpbat.admin.service.mapper.UtilisateurMapper;

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
 * Test class for the UtilisateurResource REST controller.
 *
 * @see UtilisateurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class UtilisateurResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_AGENCE = "AAAAAAAAAA";
    private static final String UPDATED_AGENCE = "BBBBBBBBBB";

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private UtilisateurMapper utilisateurMapper;

    @Inject
    private UtilisateurService utilisateurService;

    @Inject
    private UtilisateurSearchRepository utilisateurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UtilisateurResource utilisateurResource = new UtilisateurResource();
        ReflectionTestUtils.setField(utilisateurResource, "utilisateurService", utilisateurService);
        this.restUtilisateurMockMvc = MockMvcBuilders.standaloneSetup(utilisateurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur()
                .nom(DEFAULT_NOM)
                .prenom(DEFAULT_PRENOM)
                .telephone(DEFAULT_TELEPHONE)
                .agence(DEFAULT_AGENCE);
        return utilisateur;
    }

    @Before
    public void initTest() {
        utilisateurSearchRepository.deleteAll();
        utilisateur = createEntity(em);
    }

    @Test
    @Transactional
    public void createUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.utilisateurToUtilisateurDTO(utilisateur);

        restUtilisateurMockMvc.perform(post("/api/utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(utilisateurDTO)))
                .andExpect(status().isCreated());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeCreate + 1);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testUtilisateur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testUtilisateur.getAgence()).isEqualTo(DEFAULT_AGENCE);

        // Validate the Utilisateur in ElasticSearch
        Utilisateur utilisateurEs = utilisateurSearchRepository.findOne(testUtilisateur.getId());
        assertThat(utilisateurEs).isEqualToComparingFieldByField(testUtilisateur);
    }

    @Test
    @Transactional
    public void getAllUtilisateurs() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurs
        restUtilisateurMockMvc.perform(get("/api/utilisateurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
                .andExpect(jsonPath("$.[*].agence").value(hasItem(DEFAULT_AGENCE.toString())));
    }

    @Test
    @Transactional
    public void getUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.agence").value(DEFAULT_AGENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findOne(utilisateur.getId());
        updatedUtilisateur
                .nom(UPDATED_NOM)
                .prenom(UPDATED_PRENOM)
                .telephone(UPDATED_TELEPHONE)
                .agence(UPDATED_AGENCE);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.utilisateurToUtilisateurDTO(updatedUtilisateur);

        restUtilisateurMockMvc.perform(put("/api/utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(utilisateurDTO)))
                .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testUtilisateur.getAgence()).isEqualTo(UPDATED_AGENCE);

        // Validate the Utilisateur in ElasticSearch
        Utilisateur utilisateurEs = utilisateurSearchRepository.findOne(testUtilisateur.getId());
        assertThat(utilisateurEs).isEqualToComparingFieldByField(testUtilisateur);
    }

    @Test
    @Transactional
    public void deleteUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);
        int databaseSizeBeforeDelete = utilisateurRepository.findAll().size();

        // Get the utilisateur
        restUtilisateurMockMvc.perform(delete("/api/utilisateurs/{id}", utilisateur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean utilisateurExistsInEs = utilisateurSearchRepository.exists(utilisateur.getId());
        assertThat(utilisateurExistsInEs).isFalse();

        // Validate the database is empty
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);

        // Search the utilisateur
        restUtilisateurMockMvc.perform(get("/api/_search/utilisateurs?query=id:" + utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].agence").value(hasItem(DEFAULT_AGENCE.toString())));
    }
}
