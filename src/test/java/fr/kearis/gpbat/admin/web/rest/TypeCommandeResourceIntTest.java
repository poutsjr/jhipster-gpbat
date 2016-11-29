package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.TypeCommande;
import fr.kearis.gpbat.admin.repository.TypeCommandeRepository;
import fr.kearis.gpbat.admin.service.TypeCommandeService;
import fr.kearis.gpbat.admin.repository.search.TypeCommandeSearchRepository;
import fr.kearis.gpbat.admin.service.dto.TypeCommandeDTO;
import fr.kearis.gpbat.admin.service.mapper.TypeCommandeMapper;

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
 * Test class for the TypeCommandeResource REST controller.
 *
 * @see TypeCommandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class TypeCommandeResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Inject
    private TypeCommandeRepository typeCommandeRepository;

    @Inject
    private TypeCommandeMapper typeCommandeMapper;

    @Inject
    private TypeCommandeService typeCommandeService;

    @Inject
    private TypeCommandeSearchRepository typeCommandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTypeCommandeMockMvc;

    private TypeCommande typeCommande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeCommandeResource typeCommandeResource = new TypeCommandeResource();
        ReflectionTestUtils.setField(typeCommandeResource, "typeCommandeService", typeCommandeService);
        this.restTypeCommandeMockMvc = MockMvcBuilders.standaloneSetup(typeCommandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeCommande createEntity(EntityManager em) {
        TypeCommande typeCommande = new TypeCommande()
                .nom(DEFAULT_NOM);
        return typeCommande;
    }

    @Before
    public void initTest() {
        typeCommandeSearchRepository.deleteAll();
        typeCommande = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeCommande() throws Exception {
        int databaseSizeBeforeCreate = typeCommandeRepository.findAll().size();

        // Create the TypeCommande
        TypeCommandeDTO typeCommandeDTO = typeCommandeMapper.typeCommandeToTypeCommandeDTO(typeCommande);

        restTypeCommandeMockMvc.perform(post("/api/type-commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeCommandeDTO)))
                .andExpect(status().isCreated());

        // Validate the TypeCommande in the database
        List<TypeCommande> typeCommandes = typeCommandeRepository.findAll();
        assertThat(typeCommandes).hasSize(databaseSizeBeforeCreate + 1);
        TypeCommande testTypeCommande = typeCommandes.get(typeCommandes.size() - 1);
        assertThat(testTypeCommande.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the TypeCommande in ElasticSearch
        TypeCommande typeCommandeEs = typeCommandeSearchRepository.findOne(testTypeCommande.getId());
        assertThat(typeCommandeEs).isEqualToComparingFieldByField(testTypeCommande);
    }

    @Test
    @Transactional
    public void getAllTypeCommandes() throws Exception {
        // Initialize the database
        typeCommandeRepository.saveAndFlush(typeCommande);

        // Get all the typeCommandes
        restTypeCommandeMockMvc.perform(get("/api/type-commandes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeCommande.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getTypeCommande() throws Exception {
        // Initialize the database
        typeCommandeRepository.saveAndFlush(typeCommande);

        // Get the typeCommande
        restTypeCommandeMockMvc.perform(get("/api/type-commandes/{id}", typeCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeCommande.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeCommande() throws Exception {
        // Get the typeCommande
        restTypeCommandeMockMvc.perform(get("/api/type-commandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeCommande() throws Exception {
        // Initialize the database
        typeCommandeRepository.saveAndFlush(typeCommande);
        typeCommandeSearchRepository.save(typeCommande);
        int databaseSizeBeforeUpdate = typeCommandeRepository.findAll().size();

        // Update the typeCommande
        TypeCommande updatedTypeCommande = typeCommandeRepository.findOne(typeCommande.getId());
        updatedTypeCommande
                .nom(UPDATED_NOM);
        TypeCommandeDTO typeCommandeDTO = typeCommandeMapper.typeCommandeToTypeCommandeDTO(updatedTypeCommande);

        restTypeCommandeMockMvc.perform(put("/api/type-commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeCommandeDTO)))
                .andExpect(status().isOk());

        // Validate the TypeCommande in the database
        List<TypeCommande> typeCommandes = typeCommandeRepository.findAll();
        assertThat(typeCommandes).hasSize(databaseSizeBeforeUpdate);
        TypeCommande testTypeCommande = typeCommandes.get(typeCommandes.size() - 1);
        assertThat(testTypeCommande.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the TypeCommande in ElasticSearch
        TypeCommande typeCommandeEs = typeCommandeSearchRepository.findOne(testTypeCommande.getId());
        assertThat(typeCommandeEs).isEqualToComparingFieldByField(testTypeCommande);
    }

    @Test
    @Transactional
    public void deleteTypeCommande() throws Exception {
        // Initialize the database
        typeCommandeRepository.saveAndFlush(typeCommande);
        typeCommandeSearchRepository.save(typeCommande);
        int databaseSizeBeforeDelete = typeCommandeRepository.findAll().size();

        // Get the typeCommande
        restTypeCommandeMockMvc.perform(delete("/api/type-commandes/{id}", typeCommande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeCommandeExistsInEs = typeCommandeSearchRepository.exists(typeCommande.getId());
        assertThat(typeCommandeExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeCommande> typeCommandes = typeCommandeRepository.findAll();
        assertThat(typeCommandes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeCommande() throws Exception {
        // Initialize the database
        typeCommandeRepository.saveAndFlush(typeCommande);
        typeCommandeSearchRepository.save(typeCommande);

        // Search the typeCommande
        restTypeCommandeMockMvc.perform(get("/api/_search/type-commandes?query=id:" + typeCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }
}
