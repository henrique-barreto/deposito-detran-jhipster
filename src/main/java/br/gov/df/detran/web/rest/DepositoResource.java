package br.gov.df.detran.web.rest;

import br.gov.df.detran.domain.Deposito;
import br.gov.df.detran.repository.DepositoRepository;
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
 * REST controller for managing {@link br.gov.df.detran.domain.Deposito}.
 */
@RestController
@RequestMapping("/api")
public class DepositoResource {

    private final Logger log = LoggerFactory.getLogger(DepositoResource.class);

    private static final String ENTITY_NAME = "deposito";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepositoRepository depositoRepository;

    public DepositoResource(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    /**
     * {@code POST  /depositos} : Create a new deposito.
     *
     * @param deposito the deposito to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deposito, or with status {@code 400 (Bad Request)} if the deposito has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depositos")
    public ResponseEntity<Deposito> createDeposito(@RequestBody Deposito deposito) throws URISyntaxException {
        log.debug("REST request to save Deposito : {}", deposito);
        if (deposito.getId() != null) {
            throw new BadRequestAlertException("A new deposito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deposito result = depositoRepository.save(deposito);
        return ResponseEntity.created(new URI("/api/depositos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depositos} : Updates an existing deposito.
     *
     * @param deposito the deposito to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deposito,
     * or with status {@code 400 (Bad Request)} if the deposito is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deposito couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depositos")
    public ResponseEntity<Deposito> updateDeposito(@RequestBody Deposito deposito) throws URISyntaxException {
        log.debug("REST request to update Deposito : {}", deposito);
        if (deposito.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Deposito result = depositoRepository.save(deposito);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deposito.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /depositos} : get all the depositos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depositos in body.
     */
    @GetMapping("/depositos")
    public ResponseEntity<List<Deposito>> getAllDepositos(Pageable pageable) {
        log.debug("REST request to get a page of Depositos");
        Page<Deposito> page = depositoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depositos/:id} : get the "id" deposito.
     *
     * @param id the id of the deposito to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deposito, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depositos/{id}")
    public ResponseEntity<Deposito> getDeposito(@PathVariable Long id) {
        log.debug("REST request to get Deposito : {}", id);
        Optional<Deposito> deposito = depositoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deposito);
    }

    /**
     * {@code DELETE  /depositos/:id} : delete the "id" deposito.
     *
     * @param id the id of the deposito to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depositos/{id}")
    public ResponseEntity<Void> deleteDeposito(@PathVariable Long id) {
        log.debug("REST request to delete Deposito : {}", id);
        depositoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
