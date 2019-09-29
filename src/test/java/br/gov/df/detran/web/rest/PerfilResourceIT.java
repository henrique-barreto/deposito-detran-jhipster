package br.gov.df.detran.web.rest;

import br.gov.df.detran.DepositoDetranJhipsterApp;
import br.gov.df.detran.domain.Perfil;
import br.gov.df.detran.repository.PerfilRepository;
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
 * Integration tests for the {@link PerfilResource} REST controller.
 */
@SpringBootTest(classes = DepositoDetranJhipsterApp.class)
public class PerfilResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    @Autowired
    private PerfilRepository perfilRepository;

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

    private MockMvc restPerfilMockMvc;

    private Perfil perfil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PerfilResource perfilResource = new PerfilResource(perfilRepository);
        this.restPerfilMockMvc = MockMvcBuilders.standaloneSetup(perfilResource)
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
    public static Perfil createEntity(EntityManager em) {
        Perfil perfil = new Perfil()
            .nome(DEFAULT_NOME)
            .ativo(DEFAULT_ATIVO);
        return perfil;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perfil createUpdatedEntity(EntityManager em) {
        Perfil perfil = new Perfil()
            .nome(UPDATED_NOME)
            .ativo(UPDATED_ATIVO);
        return perfil;
    }

    @BeforeEach
    public void initTest() {
        perfil = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerfil() throws Exception {
        int databaseSizeBeforeCreate = perfilRepository.findAll().size();

        // Create the Perfil
        restPerfilMockMvc.perform(post("/api/perfils")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perfil)))
            .andExpect(status().isCreated());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeCreate + 1);
        Perfil testPerfil = perfilList.get(perfilList.size() - 1);
        assertThat(testPerfil.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPerfil.isAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    public void createPerfilWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = perfilRepository.findAll().size();

        // Create the Perfil with an existing ID
        perfil.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerfilMockMvc.perform(post("/api/perfils")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perfil)))
            .andExpect(status().isBadRequest());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPerfils() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList
        restPerfilMockMvc.perform(get("/api/perfils?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPerfil() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get the perfil
        restPerfilMockMvc.perform(get("/api/perfils/{id}", perfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(perfil.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPerfil() throws Exception {
        // Get the perfil
        restPerfilMockMvc.perform(get("/api/perfils/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerfil() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();

        // Update the perfil
        Perfil updatedPerfil = perfilRepository.findById(perfil.getId()).get();
        // Disconnect from session so that the updates on updatedPerfil are not directly saved in db
        em.detach(updatedPerfil);
        updatedPerfil
            .nome(UPDATED_NOME)
            .ativo(UPDATED_ATIVO);

        restPerfilMockMvc.perform(put("/api/perfils")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPerfil)))
            .andExpect(status().isOk());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
        Perfil testPerfil = perfilList.get(perfilList.size() - 1);
        assertThat(testPerfil.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPerfil.isAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    public void updateNonExistingPerfil() throws Exception {
        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();

        // Create the Perfil

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilMockMvc.perform(put("/api/perfils")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perfil)))
            .andExpect(status().isBadRequest());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePerfil() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        int databaseSizeBeforeDelete = perfilRepository.findAll().size();

        // Delete the perfil
        restPerfilMockMvc.perform(delete("/api/perfils/{id}", perfil.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Perfil.class);
        Perfil perfil1 = new Perfil();
        perfil1.setId(1L);
        Perfil perfil2 = new Perfil();
        perfil2.setId(perfil1.getId());
        assertThat(perfil1).isEqualTo(perfil2);
        perfil2.setId(2L);
        assertThat(perfil1).isNotEqualTo(perfil2);
        perfil1.setId(null);
        assertThat(perfil1).isNotEqualTo(perfil2);
    }
}
