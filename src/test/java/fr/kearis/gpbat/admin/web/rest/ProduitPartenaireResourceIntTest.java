package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.ProduitPartenaire;
import fr.kearis.gpbat.admin.repository.ProduitPartenaireRepository;
import fr.kearis.gpbat.admin.service.ProduitPartenaireService;
import fr.kearis.gpbat.admin.repository.search.ProduitPartenaireSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ProduitPartenaireDTO;
import fr.kearis.gpbat.admin.service.mapper.ProduitPartenaireMapper;

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
 * Test class for the ProduitPartenaireResource REST controller.
 *
 * @see ProduitPartenaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class ProduitPartenaireResourceIntTest {

    private static final String DEFAULT_ARTICLE = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final UniteMetrique DEFAULT_UNITE = UniteMetrique.MC;
    private static final UniteMetrique UPDATED_UNITE = UniteMetrique.ML;

    private static final Float DEFAULT_PRIX = 1F;
    private static final Float UPDATED_PRIX = 2F;

    @Inject
    private ProduitPartenaireRepository produitPartenaireRepository;

    @Inject
    private ProduitPartenaireMapper produitPartenaireMapper;

    @Inject
    private ProduitPartenaireService produitPartenaireService;

    @Inject
    private ProduitPartenaireSearchRepository produitPartenaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProduitPartenaireMockMvc;

    private ProduitPartenaire produitPartenaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProduitPartenaireResource produitPartenaireResource = new ProduitPartenaireResource();
        ReflectionTestUtils.setField(produitPartenaireResource, "produitPartenaireService", produitPartenaireService);
        this.restProduitPartenaireMockMvc = MockMvcBuilders.standaloneSetup(produitPartenaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProduitPartenaire createEntity(EntityManager em) {
        ProduitPartenaire produitPartenaire = new ProduitPartenaire()
                .article(DEFAULT_ARTICLE)
                .libelle(DEFAULT_LIBELLE)
                .unite(DEFAULT_UNITE)
                .prix(DEFAULT_PRIX);
        return produitPartenaire;
    }

    @Before
    public void initTest() {
        produitPartenaireSearchRepository.deleteAll();
        produitPartenaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduitPartenaire() throws Exception {
        int databaseSizeBeforeCreate = produitPartenaireRepository.findAll().size();

        // Create the ProduitPartenaire
        ProduitPartenaireDTO produitPartenaireDTO = produitPartenaireMapper.produitPartenaireToProduitPartenaireDTO(produitPartenaire);

        restProduitPartenaireMockMvc.perform(post("/api/produit-partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produitPartenaireDTO)))
                .andExpect(status().isCreated());

        // Validate the ProduitPartenaire in the database
        List<ProduitPartenaire> produitPartenaires = produitPartenaireRepository.findAll();
        assertThat(produitPartenaires).hasSize(databaseSizeBeforeCreate + 1);
        ProduitPartenaire testProduitPartenaire = produitPartenaires.get(produitPartenaires.size() - 1);
        assertThat(testProduitPartenaire.getArticle()).isEqualTo(DEFAULT_ARTICLE);
        assertThat(testProduitPartenaire.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testProduitPartenaire.getUnite()).isEqualTo(DEFAULT_UNITE);
        assertThat(testProduitPartenaire.getPrix()).isEqualTo(DEFAULT_PRIX);

        // Validate the ProduitPartenaire in ElasticSearch
        ProduitPartenaire produitPartenaireEs = produitPartenaireSearchRepository.findOne(testProduitPartenaire.getId());
        assertThat(produitPartenaireEs).isEqualToComparingFieldByField(testProduitPartenaire);
    }

    @Test
    @Transactional
    public void getAllProduitPartenaires() throws Exception {
        // Initialize the database
        produitPartenaireRepository.saveAndFlush(produitPartenaire);

        // Get all the produitPartenaires
        restProduitPartenaireMockMvc.perform(get("/api/produit-partenaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(produitPartenaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].article").value(hasItem(DEFAULT_ARTICLE.toString())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].unite").value(hasItem(DEFAULT_UNITE.toString())))
                .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }

    @Test
    @Transactional
    public void getProduitPartenaire() throws Exception {
        // Initialize the database
        produitPartenaireRepository.saveAndFlush(produitPartenaire);

        // Get the produitPartenaire
        restProduitPartenaireMockMvc.perform(get("/api/produit-partenaires/{id}", produitPartenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(produitPartenaire.getId().intValue()))
            .andExpect(jsonPath("$.article").value(DEFAULT_ARTICLE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.unite").value(DEFAULT_UNITE.toString()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProduitPartenaire() throws Exception {
        // Get the produitPartenaire
        restProduitPartenaireMockMvc.perform(get("/api/produit-partenaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduitPartenaire() throws Exception {
        // Initialize the database
        produitPartenaireRepository.saveAndFlush(produitPartenaire);
        produitPartenaireSearchRepository.save(produitPartenaire);
        int databaseSizeBeforeUpdate = produitPartenaireRepository.findAll().size();

        // Update the produitPartenaire
        ProduitPartenaire updatedProduitPartenaire = produitPartenaireRepository.findOne(produitPartenaire.getId());
        updatedProduitPartenaire
                .article(UPDATED_ARTICLE)
                .libelle(UPDATED_LIBELLE)
                .unite(UPDATED_UNITE)
                .prix(UPDATED_PRIX);
        ProduitPartenaireDTO produitPartenaireDTO = produitPartenaireMapper.produitPartenaireToProduitPartenaireDTO(updatedProduitPartenaire);

        restProduitPartenaireMockMvc.perform(put("/api/produit-partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produitPartenaireDTO)))
                .andExpect(status().isOk());

        // Validate the ProduitPartenaire in the database
        List<ProduitPartenaire> produitPartenaires = produitPartenaireRepository.findAll();
        assertThat(produitPartenaires).hasSize(databaseSizeBeforeUpdate);
        ProduitPartenaire testProduitPartenaire = produitPartenaires.get(produitPartenaires.size() - 1);
        assertThat(testProduitPartenaire.getArticle()).isEqualTo(UPDATED_ARTICLE);
        assertThat(testProduitPartenaire.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testProduitPartenaire.getUnite()).isEqualTo(UPDATED_UNITE);
        assertThat(testProduitPartenaire.getPrix()).isEqualTo(UPDATED_PRIX);

        // Validate the ProduitPartenaire in ElasticSearch
        ProduitPartenaire produitPartenaireEs = produitPartenaireSearchRepository.findOne(testProduitPartenaire.getId());
        assertThat(produitPartenaireEs).isEqualToComparingFieldByField(testProduitPartenaire);
    }

    @Test
    @Transactional
    public void deleteProduitPartenaire() throws Exception {
        // Initialize the database
        produitPartenaireRepository.saveAndFlush(produitPartenaire);
        produitPartenaireSearchRepository.save(produitPartenaire);
        int databaseSizeBeforeDelete = produitPartenaireRepository.findAll().size();

        // Get the produitPartenaire
        restProduitPartenaireMockMvc.perform(delete("/api/produit-partenaires/{id}", produitPartenaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean produitPartenaireExistsInEs = produitPartenaireSearchRepository.exists(produitPartenaire.getId());
        assertThat(produitPartenaireExistsInEs).isFalse();

        // Validate the database is empty
        List<ProduitPartenaire> produitPartenaires = produitPartenaireRepository.findAll();
        assertThat(produitPartenaires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProduitPartenaire() throws Exception {
        // Initialize the database
        produitPartenaireRepository.saveAndFlush(produitPartenaire);
        produitPartenaireSearchRepository.save(produitPartenaire);

        // Search the produitPartenaire
        restProduitPartenaireMockMvc.perform(get("/api/_search/produit-partenaires?query=id:" + produitPartenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produitPartenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].article").value(hasItem(DEFAULT_ARTICLE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].unite").value(hasItem(DEFAULT_UNITE.toString())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }
}
