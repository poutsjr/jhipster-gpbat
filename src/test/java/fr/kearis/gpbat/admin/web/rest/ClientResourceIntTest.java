package fr.kearis.gpbat.admin.web.rest;

import fr.kearis.gpbat.admin.GpbatApp;

import fr.kearis.gpbat.admin.domain.Client;
import fr.kearis.gpbat.admin.repository.ClientRepository;
import fr.kearis.gpbat.admin.service.ClientService;
import fr.kearis.gpbat.admin.repository.search.ClientSearchRepository;
import fr.kearis.gpbat.admin.service.dto.ClientDTO;
import fr.kearis.gpbat.admin.service.mapper.ClientMapper;

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
 * Test class for the ClientResource REST controller.
 *
 * @see ClientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpbatApp.class)
public class ClientResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientMapper clientMapper;

    @Inject
    private ClientService clientService;

    @Inject
    private ClientSearchRepository clientSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClientMockMvc;

    private Client client;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientResource clientResource = new ClientResource();
        ReflectionTestUtils.setField(clientResource, "clientService", clientService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
                .nom(DEFAULT_NOM);
        return client;
    }

    @Before
    public void initTest() {
        clientSearchRepository.deleteAll();
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.clientToClientDTO(client);

        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
                .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Client in ElasticSearch
        Client clientEs = clientSearchRepository.findOne(testClient.getId());
        assertThat(clientEs).isEqualToComparingFieldByField(testClient);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clients
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        clientSearchRepository.save(client);
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findOne(client.getId());
        updatedClient
                .nom(UPDATED_NOM);
        ClientDTO clientDTO = clientMapper.clientToClientDTO(updatedClient);

        restClientMockMvc.perform(put("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
                .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Client in ElasticSearch
        Client clientEs = clientSearchRepository.findOne(testClient.getId());
        assertThat(clientEs).isEqualToComparingFieldByField(testClient);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        clientSearchRepository.save(client);
        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clientExistsInEs = clientSearchRepository.exists(client.getId());
        assertThat(clientExistsInEs).isFalse();

        // Validate the database is empty
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        clientSearchRepository.save(client);

        // Search the client
        restClientMockMvc.perform(get("/api/_search/clients?query=id:" + client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }
}
