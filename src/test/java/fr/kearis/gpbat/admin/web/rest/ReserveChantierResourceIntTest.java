package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.ReserveChantier;
import fr.kearis.gpbat.admin.repository.ReserveChantierRepository;
import fr.kearis.gpbat.admin.service.ReserveChantierService;
import fr.kearis.gpbat.admin.repository.search.ReserveChantierSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ReserveChantierDTO;
import fr.kearis.gpbat.admin.service.mapper.ReserveChantierMapper;

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
 * Test class for the ReserveChantierResource REST controller.
 *
 * @see ReserveChantierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class ReserveChantierResourceIntTest {

    private static final String DEFAULT_RESERVES = "AAAAAAAAAA";
    private static final String UPDATED_RESERVES = "BBBBBBBBBB";

    @Inject
    private ReserveChantierRepository reserveChantierRepository;

    @Inject
    private ReserveChantierMapper reserveChantierMapper;

    @Inject
    private ReserveChantierService reserveChantierService;

    @Inject
    private ReserveChantierSearchRepository reserveChantierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restReserveChantierMockMvc;

    private ReserveChantier reserveChantier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReserveChantierResource reserveChantierResource = new ReserveChantierResource();
        ReflectionTestUtils.setField(reserveChantierResource, "reserveChantierService", reserveChantierService);
        this.restReserveChantierMockMvc = MockMvcBuilders.standaloneSetup(reserveChantierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReserveChantier createEntity(EntityManager em) {
        ReserveChantier reserveChantier = new ReserveChantier()
                .reserves(DEFAULT_RESERVES);
        return reserveChantier;
    }

    @Before
    public void initTest() {
        reserveChantierSearchRepository.deleteAll();
        reserveChantier = createEntity(em);
    }

    @Test
    @Transactional
    public void createReserveChantier() throws Exception {
        int databaseSizeBeforeCreate = reserveChantierRepository.findAll().size();

        // Create the ReserveChantier
        ReserveChantierDTO reserveChantierDTO = reserveChantierMapper.reserveChantierToReserveChantierDTO(reserveChantier);

        restReserveChantierMockMvc.perform(post("/api/reserve-chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reserveChantierDTO)))
                .andExpect(status().isCreated());

        // Validate the ReserveChantier in the database
        List<ReserveChantier> reserveChantiers = reserveChantierRepository.findAll();
        assertThat(reserveChantiers).hasSize(databaseSizeBeforeCreate + 1);
        ReserveChantier testReserveChantier = reserveChantiers.get(reserveChantiers.size() - 1);
        assertThat(testReserveChantier.getReserves()).isEqualTo(DEFAULT_RESERVES);

        // Validate the ReserveChantier in ElasticSearch
        ReserveChantier reserveChantierEs = reserveChantierSearchRepository.findOne(testReserveChantier.getId());
        assertThat(reserveChantierEs).isEqualToComparingFieldByField(testReserveChantier);
    }

    @Test
    @Transactional
    public void getAllReserveChantiers() throws Exception {
        // Initialize the database
        reserveChantierRepository.saveAndFlush(reserveChantier);

        // Get all the reserveChantiers
        restReserveChantierMockMvc.perform(get("/api/reserve-chantiers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reserveChantier.getId().intValue())))
                .andExpect(jsonPath("$.[*].reserves").value(hasItem(DEFAULT_RESERVES.toString())));
    }

    @Test
    @Transactional
    public void getReserveChantier() throws Exception {
        // Initialize the database
        reserveChantierRepository.saveAndFlush(reserveChantier);

        // Get the reserveChantier
        restReserveChantierMockMvc.perform(get("/api/reserve-chantiers/{id}", reserveChantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reserveChantier.getId().intValue()))
            .andExpect(jsonPath("$.reserves").value(DEFAULT_RESERVES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReserveChantier() throws Exception {
        // Get the reserveChantier
        restReserveChantierMockMvc.perform(get("/api/reserve-chantiers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReserveChantier() throws Exception {
        // Initialize the database
        reserveChantierRepository.saveAndFlush(reserveChantier);
        reserveChantierSearchRepository.save(reserveChantier);
        int databaseSizeBeforeUpdate = reserveChantierRepository.findAll().size();

        // Update the reserveChantier
        ReserveChantier updatedReserveChantier = reserveChantierRepository.findOne(reserveChantier.getId());
        updatedReserveChantier
                .reserves(UPDATED_RESERVES);
        ReserveChantierDTO reserveChantierDTO = reserveChantierMapper.reserveChantierToReserveChantierDTO(updatedReserveChantier);

        restReserveChantierMockMvc.perform(put("/api/reserve-chantiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reserveChantierDTO)))
                .andExpect(status().isOk());

        // Validate the ReserveChantier in the database
        List<ReserveChantier> reserveChantiers = reserveChantierRepository.findAll();
        assertThat(reserveChantiers).hasSize(databaseSizeBeforeUpdate);
        ReserveChantier testReserveChantier = reserveChantiers.get(reserveChantiers.size() - 1);
        assertThat(testReserveChantier.getReserves()).isEqualTo(UPDATED_RESERVES);

        // Validate the ReserveChantier in ElasticSearch
        ReserveChantier reserveChantierEs = reserveChantierSearchRepository.findOne(testReserveChantier.getId());
        assertThat(reserveChantierEs).isEqualToComparingFieldByField(testReserveChantier);
    }

    @Test
    @Transactional
    public void deleteReserveChantier() throws Exception {
        // Initialize the database
        reserveChantierRepository.saveAndFlush(reserveChantier);
        reserveChantierSearchRepository.save(reserveChantier);
        int databaseSizeBeforeDelete = reserveChantierRepository.findAll().size();

        // Get the reserveChantier
        restReserveChantierMockMvc.perform(delete("/api/reserve-chantiers/{id}", reserveChantier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean reserveChantierExistsInEs = reserveChantierSearchRepository.exists(reserveChantier.getId());
        assertThat(reserveChantierExistsInEs).isFalse();

        // Validate the database is empty
        List<ReserveChantier> reserveChantiers = reserveChantierRepository.findAll();
        assertThat(reserveChantiers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReserveChantier() throws Exception {
        // Initialize the database
        reserveChantierRepository.saveAndFlush(reserveChantier);
        reserveChantierSearchRepository.save(reserveChantier);

        // Search the reserveChantier
        restReserveChantierMockMvc.perform(get("/api/_search/reserve-chantiers?query=id:" + reserveChantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserveChantier.getId().intValue())))
            .andExpect(jsonPath("$.[*].reserves").value(hasItem(DEFAULT_RESERVES.toString())));
    }
}
