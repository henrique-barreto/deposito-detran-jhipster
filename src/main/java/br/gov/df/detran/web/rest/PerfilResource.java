package br.gov.df.detran.web.rest;

import br.gov.df.detran.domain.Perfil;
import br.gov.df.detran.repository.PerfilRepository;
import br.gov.df.detran.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.gov.df.detran.domain.Perfil}.
 */
@RestController
@RequestMapping("/api")
public class PerfilResource {

    private final Logger log = LoggerFactory.getLogger(PerfilResource.class);

    private static final String ENTITY_NAME = "perfil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerfilRepository perfilRepository;

    public PerfilResource(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    /**
     * {@code POST  /perfils} : Create a new perfil.
     *
     * @param perfil the perfil to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perfil, or with status {@code 400 (Bad Request)} if the perfil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/perfils")
    public ResponseEntity<Perfil> createPerfil(@RequestBody Perfil perfil) throws URISyntaxException {
        log.debug("REST request to save Perfil : {}", perfil);
        if (perfil.getId() != null) {
            throw new BadRequestAlertException("A new perfil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Perfil result = perfilRepository.save(perfil);
        return ResponseEntity.created(new URI("/api/perfils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /perfils} : Updates an existing perfil.
     *
     * @param perfil the perfil to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfil,
     * or with status {@code 400 (Bad Request)} if the perfil is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perfil couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/perfils")
    public ResponseEntity<Perfil> updatePerfil(@RequestBody Perfil perfil) throws URISyntaxException {
        log.debug("REST request to update Perfil : {}", perfil);
        if (perfil.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Perfil result = perfilRepository.save(perfil);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, perfil.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /perfils} : get all the perfils.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perfils in body.
     */
    @GetMapping("/perfils")
    public ResponseEntity<List<Perfil>> getAllPerfils(Pageable pageable) {
        log.debug("REST request to get a page of Perfils");
        Page<Perfil> page = perfilRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /perfils/:id} : get the "id" perfil.
     *
     * @param id the id of the perfil to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perfil, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/perfils/{id}")
    public ResponseEntity<Perfil> getPerfil(@PathVariable Long id) {
        log.debug("REST request to get Perfil : {}", id);
        Optional<Perfil> perfil = perfilRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(perfil);
    }

    /**
     * {@code DELETE  /perfils/:id} : delete the "id" perfil.
     *
     * @param id the id of the perfil to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/perfils/{id}")
    public ResponseEntity<Void> deletePerfil(@PathVariable Long id) {
        log.debug("REST request to delete Perfil : {}", id);
        perfilRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
