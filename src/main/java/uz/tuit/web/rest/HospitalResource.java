package uz.tuit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.tuit.repository.HospitalRepository;
import uz.tuit.service.HospitalQueryService;
import uz.tuit.service.HospitalService;
import uz.tuit.service.criteria.HospitalCriteria;
import uz.tuit.service.dto.HospitalDTO;
import uz.tuit.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tuit.domain.Hospital}.
 */
@RestController
@RequestMapping("/api")
public class HospitalResource {

    private final Logger log = LoggerFactory.getLogger(HospitalResource.class);

    private static final String ENTITY_NAME = "hospital";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HospitalService hospitalService;

    private final HospitalRepository hospitalRepository;

    private final HospitalQueryService hospitalQueryService;

    public HospitalResource(
        HospitalService hospitalService,
        HospitalRepository hospitalRepository,
        HospitalQueryService hospitalQueryService
    ) {
        this.hospitalService = hospitalService;
        this.hospitalRepository = hospitalRepository;
        this.hospitalQueryService = hospitalQueryService;
    }

    /**
     * {@code POST  /hospitals} : Create a new hospital.
     *
     * @param hospitalDTO the hospitalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hospitalDTO, or with status {@code 400 (Bad Request)} if the hospital has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hospitals")
    public ResponseEntity<HospitalDTO> createHospital(@Valid @RequestBody HospitalDTO hospitalDTO) throws URISyntaxException {
        log.debug("REST request to save Hospital : {}", hospitalDTO);
        if (hospitalDTO.getId() != null) {
            throw new BadRequestAlertException("A new hospital cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HospitalDTO result = hospitalService.save(hospitalDTO);
        return ResponseEntity
            .created(new URI("/api/hospitals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hospitals/:id} : Updates an existing hospital.
     *
     * @param id the id of the hospitalDTO to save.
     * @param hospitalDTO the hospitalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospitalDTO,
     * or with status {@code 400 (Bad Request)} if the hospitalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hospitalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hospitals/{id}")
    public ResponseEntity<HospitalDTO> updateHospital(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HospitalDTO hospitalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Hospital : {}, {}", id, hospitalDTO);
        if (hospitalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hospitalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hospitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HospitalDTO result = hospitalService.update(hospitalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hospitalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hospitals/:id} : Partial updates given fields of an existing hospital, field will ignore if it is null
     *
     * @param id the id of the hospitalDTO to save.
     * @param hospitalDTO the hospitalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospitalDTO,
     * or with status {@code 400 (Bad Request)} if the hospitalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hospitalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hospitalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hospitals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HospitalDTO> partialUpdateHospital(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HospitalDTO hospitalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hospital partially : {}, {}", id, hospitalDTO);
        if (hospitalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hospitalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hospitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HospitalDTO> result = hospitalService.partialUpdate(hospitalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hospitalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hospitals} : get all the hospitals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hospitals in body.
     */
    @GetMapping("/hospitals")
    public ResponseEntity<List<HospitalDTO>> getAllHospitals(
        HospitalCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Hospitals by criteria: {}", criteria);
        Page<HospitalDTO> page = hospitalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hospitals/count} : count all the hospitals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/hospitals/count")
    public ResponseEntity<Long> countHospitals(HospitalCriteria criteria) {
        log.debug("REST request to count Hospitals by criteria: {}", criteria);
        return ResponseEntity.ok().body(hospitalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hospitals/:id} : get the "id" hospital.
     *
     * @param id the id of the hospitalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hospitalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hospitals/{id}")
    public ResponseEntity<HospitalDTO> getHospital(@PathVariable Long id) {
        log.debug("REST request to get Hospital : {}", id);
        Optional<HospitalDTO> hospitalDTO = hospitalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hospitalDTO);
    }

    /**
     * {@code DELETE  /hospitals/:id} : delete the "id" hospital.
     *
     * @param id the id of the hospitalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hospitals/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        log.debug("REST request to delete Hospital : {}", id);
        hospitalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
