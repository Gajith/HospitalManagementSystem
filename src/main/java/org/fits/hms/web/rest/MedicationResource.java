package org.fits.hms.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fits.hms.domain.Medication;
import org.fits.hms.service.MedicationService;
import org.fits.hms.web.rest.errors.BadRequestAlertException;
import org.fits.hms.web.rest.util.HeaderUtil;
import org.fits.hms.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Medication.
 */
@RestController
@RequestMapping("/api")
public class MedicationResource {

    private final Logger log = LoggerFactory.getLogger(MedicationResource.class);

    private static final String ENTITY_NAME = "medication";

    private final MedicationService medicationService;

    public MedicationResource(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    /**
     * POST  /medications : Create a new medication.
     *
     * @param medication the medication to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medication, or with status 400 (Bad Request) if the medication has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medications")
    @Timed
    public ResponseEntity<Medication> createMedication(@Valid @RequestBody Medication medication) throws URISyntaxException {
        log.debug("REST request to save Medication : {}", medication);
        if (medication.getId() != null) {
            throw new BadRequestAlertException("A new medication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medication result = medicationService.save(medication);
        return ResponseEntity.created(new URI("/api/medications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medications : Updates an existing medication.
     *
     * @param medication the medication to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medication,
     * or with status 400 (Bad Request) if the medication is not valid,
     * or with status 500 (Internal Server Error) if the medication couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medications")
    @Timed
    public ResponseEntity<Medication> updateMedication(@Valid @RequestBody Medication medication) throws URISyntaxException {
        log.debug("REST request to update Medication : {}", medication);
        if (medication.getId() == null) {
            return createMedication(medication);
        }
        Medication result = medicationService.save(medication);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medication.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medications : get all the medications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medications in body
     */
    @GetMapping("/medications")
    @Timed
    public ResponseEntity<List<Medication>> getAllMedications(Pageable pageable) {
        log.debug("REST request to get a page of Medications");
        Page<Medication> page = medicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medications/:id : get the "id" medication.
     *
     * @param id the id of the medication to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medication, or with status 404 (Not Found)
     */
    @GetMapping("/medications/{id}")
    @Timed
    public ResponseEntity<Medication> getMedication(@PathVariable Long id) {
        log.debug("REST request to get Medication : {}", id);
        Medication medication = medicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medication));
    }

    /**
     * DELETE  /medications/:id : delete the "id" medication.
     *
     * @param id the id of the medication to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medications/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        log.debug("REST request to delete Medication : {}", id);
        medicationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
