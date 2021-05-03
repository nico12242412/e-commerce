package ar.com.danaide.web.rest;

import ar.com.danaide.domain.SpecialDate;
import ar.com.danaide.repository.SpecialDateRepository;
import ar.com.danaide.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ar.com.danaide.domain.SpecialDate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpecialDateResource {

    private final Logger log = LoggerFactory.getLogger(SpecialDateResource.class);

    private static final String ENTITY_NAME = "specialDate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialDateRepository specialDateRepository;

    public SpecialDateResource(SpecialDateRepository specialDateRepository) {
        this.specialDateRepository = specialDateRepository;
    }

    /**
     * {@code POST  /special-dates} : Create a new specialDate.
     *
     * @param specialDate the specialDate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialDate, or with status {@code 400 (Bad Request)} if the specialDate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/special-dates")
    public ResponseEntity<SpecialDate> createSpecialDate(@RequestBody SpecialDate specialDate) throws URISyntaxException {
        log.debug("REST request to save SpecialDate : {}", specialDate);
        if (specialDate.getId() != null) {
            throw new BadRequestAlertException("A new specialDate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialDate result = specialDateRepository.save(specialDate);
        return ResponseEntity.created(new URI("/api/special-dates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /special-dates} : Updates an existing specialDate.
     *
     * @param specialDate the specialDate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialDate,
     * or with status {@code 400 (Bad Request)} if the specialDate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialDate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/special-dates")
    public ResponseEntity<SpecialDate> updateSpecialDate(@RequestBody SpecialDate specialDate) throws URISyntaxException {
        log.debug("REST request to update SpecialDate : {}", specialDate);
        if (specialDate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpecialDate result = specialDateRepository.save(specialDate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialDate.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /special-dates} : get all the specialDates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialDates in body.
     */
    @GetMapping("/special-dates")
    public List<SpecialDate> getAllSpecialDates() {
        log.debug("REST request to get all SpecialDates");
        return specialDateRepository.findAll();
    }

    /**
     * {@code GET  /special-dates/:id} : get the "id" specialDate.
     *
     * @param id the id of the specialDate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialDate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/special-dates/{id}")
    public ResponseEntity<SpecialDate> getSpecialDate(@PathVariable Long id) {
        log.debug("REST request to get SpecialDate : {}", id);
        Optional<SpecialDate> specialDate = specialDateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(specialDate);
    }

    /**
     * {@code DELETE  /special-dates/:id} : delete the "id" specialDate.
     *
     * @param id the id of the specialDate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/special-dates/{id}")
    public ResponseEntity<Void> deleteSpecialDate(@PathVariable Long id) {
        log.debug("REST request to delete SpecialDate : {}", id);
        specialDateRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
