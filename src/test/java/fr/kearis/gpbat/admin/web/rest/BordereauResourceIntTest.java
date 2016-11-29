package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.Bordereau;
import fr.kearis.gpbat.admin.repository.BordereauRepository;
import fr.kearis.gpbat.admin.service.BordereauService;
import fr.kearis.gpbat.admin.repository.search.BordereauSearchRepository;
import fr.kearis.gpbat.admin.service.dto.BordereauDTO;
import fr.kearis.gpbat.admin.service.mapper.BordereauMapper;

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

import fr.kearis.gpbat.admin.domain.enumeration.UniteMetrique;
/**
 * Test class for the BordereauResource REST controller.
 *
 * @see BordereauResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class BordereauResourceIntTest {

    private static final String DEFAULT_ARTICLE = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final UniteMetrique DEFAULT_UNITE = UniteMetrique.MC;
    private static final UniteMetrique UPDATED_UNITE = UniteMetrique.ML;

    private static final Float DEFAULT_PRIX = 1F;
    private static final Float UPDATED_PRIX = 2F;

    @Inject
    private BordereauRepository bordereauRepository;

    @Inject
    private BordereauMapper bordereauMapper;

    @Inject
    private BordereauService bordereauService;

    @Inject
    private BordereauSearchRepository bordereauSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBordereauMockMvc;

    private Bordereau bordereau;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BordereauResource bordereauResource = new BordereauResource();
        ReflectionTestUtils.setField(bordereauResource, "bordereauService", bordereauService);
        this.restBordereauMockMvc = MockMvcBuilders.standaloneSetup(bordereauResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bordereau createEntity(EntityManager em) {
        Bordereau bordereau = new Bordereau()
                .article(DEFAULT_ARTICLE)
                .libelle(DEFAULT_LIBELLE)
                .unite(DEFAULT_UNITE)
                .prix(DEFAULT_PRIX);
        return bordereau;
    }

    @Before
    public void initTest() {
        bordereauSearchRepository.deleteAll();
        bordereau = createEntity(em);
    }

    @Test
    @Transactional
    public void createBordereau() throws Exception {
        int databaseSizeBeforeCreate = bordereauRepository.findAll().size();

        // Create the Bordereau
        BordereauDTO bordereauDTO = bordereauMapper.bordereauToBordereauDTO(bordereau);

        restBordereauMockMvc.perform(post("/api/bordereaus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bordereauDTO)))
                .andExpect(status().isCreated());

        // Validate the Bordereau in the database
        List<Bordereau> bordereaus = bordereauRepository.findAll();
        assertThat(bordereaus).hasSize(databaseSizeBeforeCreate + 1);
        Bordereau testBordereau = bordereaus.get(bordereaus.size() - 1);
        assertThat(testBordereau.getArticle()).isEqualTo(DEFAULT_ARTICLE);
        assertThat(testBordereau.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testBordereau.getUnite()).isEqualTo(DEFAULT_UNITE);
        assertThat(testBordereau.getPrix()).isEqualTo(DEFAULT_PRIX);

        // Validate the Bordereau in ElasticSearch
        Bordereau bordereauEs = bordereauSearchRepository.findOne(testBordereau.getId());
        assertThat(bordereauEs).isEqualToComparingFieldByField(testBordereau);
    }

    @Test
    @Transactional
    public void getAllBordereaus() throws Exception {
        // Initialize the database
        bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereaus
        restBordereauMockMvc.perform(get("/api/bordereaus?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bordereau.getId().intValue())))
                .andExpect(jsonPath("$.[*].article").value(hasItem(DEFAULT_ARTICLE.toString())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].unite").value(hasItem(DEFAULT_UNITE.toString())))
                .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }

    @Test
    @Transactional
    public void getBordereau() throws Exception {
        // Initialize the database
        bordereauRepository.saveAndFlush(bordereau);

        // Get the bordereau
        restBordereauMockMvc.perform(get("/api/bordereaus/{id}", bordereau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bordereau.getId().intValue()))
            .andExpect(jsonPath("$.article").value(DEFAULT_ARTICLE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.unite").value(DEFAULT_UNITE.toString()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBordereau() throws Exception {
        // Get the bordereau
        restBordereauMockMvc.perform(get("/api/bordereaus/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBordereau() throws Exception {
        // Initialize the database
        bordereauRepository.saveAndFlush(bordereau);
        bordereauSearchRepository.save(bordereau);
        int databaseSizeBeforeUpdate = bordereauRepository.findAll().size();

        // Update the bordereau
        Bordereau updatedBordereau = bordereauRepository.findOne(bordereau.getId());
        updatedBordereau
                .article(UPDATED_ARTICLE)
                .libelle(UPDATED_LIBELLE)
                .unite(UPDATED_UNITE)
                .prix(UPDATED_PRIX);
        BordereauDTO bordereauDTO = bordereauMapper.bordereauToBordereauDTO(updatedBordereau);

        restBordereauMockMvc.perform(put("/api/bordereaus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bordereauDTO)))
                .andExpect(status().isOk());

        // Validate the Bordereau in the database
        List<Bordereau> bordereaus = bordereauRepository.findAll();
        assertThat(bordereaus).hasSize(databaseSizeBeforeUpdate);
        Bordereau testBordereau = bordereaus.get(bordereaus.size() - 1);
        assertThat(testBordereau.getArticle()).isEqualTo(UPDATED_ARTICLE);
        assertThat(testBordereau.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testBordereau.getUnite()).isEqualTo(UPDATED_UNITE);
        assertThat(testBordereau.getPrix()).isEqualTo(UPDATED_PRIX);

        // Validate the Bordereau in ElasticSearch
        Bordereau bordereauEs = bordereauSearchRepository.findOne(testBordereau.getId());
        assertThat(bordereauEs).isEqualToComparingFieldByField(testBordereau);
    }

    @Test
    @Transactional
    public void deleteBordereau() throws Exception {
        // Initialize the database
        bordereauRepository.saveAndFlush(bordereau);
        bordereauSearchRepository.save(bordereau);
        int databaseSizeBeforeDelete = bordereauRepository.findAll().size();

        // Get the bordereau
        restBordereauMockMvc.perform(delete("/api/bordereaus/{id}", bordereau.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bordereauExistsInEs = bordereauSearchRepository.exists(bordereau.getId());
        assertThat(bordereauExistsInEs).isFalse();

        // Validate the database is empty
        List<Bordereau> bordereaus = bordereauRepository.findAll();
        assertThat(bordereaus).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBordereau() throws Exception {
        // Initialize the database
        bordereauRepository.saveAndFlush(bordereau);
        bordereauSearchRepository.save(bordereau);

        // Search the bordereau
        restBordereauMockMvc.perform(get("/api/_search/bordereaus?query=id:" + bordereau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bordereau.getId().intValue())))
            .andExpect(jsonPath("$.[*].article").value(hasItem(DEFAULT_ARTICLE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].unite").value(hasItem(DEFAULT_UNITE.toString())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }
}
