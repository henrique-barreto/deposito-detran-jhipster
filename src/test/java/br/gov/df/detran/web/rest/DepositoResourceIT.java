package br.gov.df.detran.web.rest;

import br.gov.df.detran.DepositoDetranJhipsterApp;
import br.gov.df.detran.domain.Deposito;
import br.gov.df.detran.repository.DepositoRepository;
import br.gov.df.detran.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static br.gov.df.detran.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DepositoResource} REST controller.
 */
@SpringBootTest(classes = DepositoDetranJhipsterApp.class)
public class DepositoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    @Autowired
    private DepositoRepository depositoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDepositoMockMvc;

    private Deposito deposito;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepositoResource depositoResource = new DepositoResource(depositoRepository);
        this.restDepositoMockMvc = MockMvcBuilders.standaloneSetup(depositoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposito createEntity(EntityManager em) {
        Deposito deposito = new Deposito()
            .nome(DEFAULT_NOME)
            .ativo(DEFAULT_ATIVO);
        return deposito;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposito createUpdatedEntity(EntityManager em) {
        Deposito deposito = new Deposito()
            .nome(UPDATED_NOME)
            .ativo(UPDATED_ATIVO);
        return deposito;
    }

    @BeforeEach
    public void initTest() {
        deposito = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeposito() throws Exception {
        int databaseSizeBeforeCreate = depositoRepository.findAll().size();

        // Create the Deposito
        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposito)))
            .andExpect(status().isCreated());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeCreate + 1);
        Deposito testDeposito = depositoList.get(depositoList.size() - 1);
        assertThat(testDeposito.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDeposito.isAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    public void createDepositoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depositoRepository.findAll().size();

        // Create the Deposito with an existing ID
        deposito.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposito)))
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDepositos() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList
        restDepositoMockMvc.perform(get("/api/depositos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposito.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        // Get the deposito
        restDepositoMockMvc.perform(get("/api/depositos/{id}", deposito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deposito.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDeposito() throws Exception {
        // Get the deposito
        restDepositoMockMvc.perform(get("/api/depositos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();

        // Update the deposito
        Deposito updatedDeposito = depositoRepository.findById(deposito.getId()).get();
        // Disconnect from session so that the updates on updatedDeposito are not directly saved in db
        em.detach(updatedDeposito);
        updatedDeposito
            .nome(UPDATED_NOME)
            .ativo(UPDATED_ATIVO);

        restDepositoMockMvc.perform(put("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeposito)))
            .andExpect(status().isOk());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
        Deposito testDeposito = depositoList.get(depositoList.size() - 1);
        assertThat(testDeposito.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDeposito.isAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    public void updateNonExistingDeposito() throws Exception {
        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();

        // Create the Deposito

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoMockMvc.perform(put("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposito)))
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        int databaseSizeBeforeDelete = depositoRepository.findAll().size();

        // Delete the deposito
        restDepositoMockMvc.perform(delete("/api/depositos/{id}", deposito.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deposito.class);
        Deposito deposito1 = new Deposito();
        deposito1.setId(1L);
        Deposito deposito2 = new Deposito();
        deposito2.setId(deposito1.getId());
        assertThat(deposito1).isEqualTo(deposito2);
        deposito2.setId(2L);
        assertThat(deposito1).isNotEqualTo(deposito2);
        deposito1.setId(null);
        assertThat(deposito1).isNotEqualTo(deposito2);
    }
}
