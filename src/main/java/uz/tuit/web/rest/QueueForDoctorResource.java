package uz.tuit.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
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
import uz.tuit.repository.QueueForDoctorRepository;
import uz.tuit.service.QrCodeService;
import uz.tuit.service.QueueForDoctorQueryService;
import uz.tuit.service.QueueForDoctorService;
import uz.tuit.service.criteria.QueueForDoctorCriteria;
import uz.tuit.service.dto.QueueForDoctorDTO;
import uz.tuit.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tuit.domain.QueueForDoctor}.
 */
@RestController
@RequestMapping("/api")
public class QueueForDoctorResource {

    private final Logger log = LoggerFactory.getLogger(QueueForDoctorResource.class);

    private static final String ENTITY_NAME = "queueForDoctor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QueueForDoctorService queueForDoctorService;

    private final QueueForDoctorRepository queueForDoctorRepository;

    private final QueueForDoctorQueryService queueForDoctorQueryService;

    private final QrCodeService qrCodeService;

    public QueueForDoctorResource(
        QueueForDoctorService queueForDoctorService,
        QueueForDoctorRepository queueForDoctorRepository,
        QueueForDoctorQueryService queueForDoctorQueryService,
        QrCodeService qrCodeService
    ) {
        this.queueForDoctorService = queueForDoctorService;
        this.queueForDoctorRepository = queueForDoctorRepository;
        this.queueForDoctorQueryService = queueForDoctorQueryService;
        this.qrCodeService = qrCodeService;
    }

    /**
     * {@code POST  /queue-for-doctors} : Create a new queueForDoctor.
     *
     * @param queueForDoctorDTO the queueForDoctorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new queueForDoctorDTO, or with status {@code 400 (Bad Request)} if the queueForDoctor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/queue-for-doctors")
    public ResponseEntity<QueueForDoctorDTO> createQueueForDoctor(@Valid @RequestBody QueueForDoctorDTO queueForDoctorDTO)
        throws URISyntaxException {
        log.debug("REST request to save QueueForDoctor : {}", queueForDoctorDTO);
        if (queueForDoctorDTO.getId() != null) {
            throw new BadRequestAlertException("A new queueForDoctor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QueueForDoctorDTO result = queueForDoctorService.save(queueForDoctorDTO);
        return ResponseEntity
            .created(new URI("/api/queue-for-doctors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /queue-for-doctors/:id} : Updates an existing queueForDoctor.
     *
     * @param id the id of the queueForDoctorDTO to save.
     * @param queueForDoctorDTO the queueForDoctorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queueForDoctorDTO,
     * or with status {@code 400 (Bad Request)} if the queueForDoctorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the queueForDoctorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/queue-for-doctors/{id}")
    public ResponseEntity<QueueForDoctorDTO> updateQueueForDoctor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QueueForDoctorDTO queueForDoctorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QueueForDoctor : {}, {}", id, queueForDoctorDTO);
        if (queueForDoctorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, queueForDoctorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!queueForDoctorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QueueForDoctorDTO result = queueForDoctorService.update(queueForDoctorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, queueForDoctorDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/qrcode/{number}")
    public void getQrCode(@PathVariable("number") String number, HttpServletResponse response) throws IOException {
        byte[] qrCode = qrCodeService.generateQRCode(number, 300, 300);
        response.getOutputStream().write(qrCode);
    }

    /**
     * {@code PATCH  /queue-for-doctors/:id} : Partial updates given fields of an existing queueForDoctor, field will ignore if it is null
     *
     * @param id the id of the queueForDoctorDTO to save.
     * @param queueForDoctorDTO the queueForDoctorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queueForDoctorDTO,
     * or with status {@code 400 (Bad Request)} if the queueForDoctorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the queueForDoctorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the queueForDoctorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/queue-for-doctors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QueueForDoctorDTO> partialUpdateQueueForDoctor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QueueForDoctorDTO queueForDoctorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QueueForDoctor partially : {}, {}", id, queueForDoctorDTO);
        if (queueForDoctorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, queueForDoctorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!queueForDoctorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QueueForDoctorDTO> result = queueForDoctorService.partialUpdate(queueForDoctorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, queueForDoctorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /queue-for-doctors} : get all the queueForDoctors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of queueForDoctors in body.
     */
    @GetMapping("/queue-for-doctors")
    public ResponseEntity<List<QueueForDoctorDTO>> getAllQueueForDoctors(
        QueueForDoctorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get QueueForDoctors by criteria: {}", criteria);
        Page<QueueForDoctorDTO> page = queueForDoctorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /queue-for-doctors/count} : count all the queueForDoctors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/queue-for-doctors/count")
    public ResponseEntity<Long> countQueueForDoctors(QueueForDoctorCriteria criteria) {
        log.debug("REST request to count QueueForDoctors by criteria: {}", criteria);
        return ResponseEntity.ok().body(queueForDoctorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /queue-for-doctors/:id} : get the "id" queueForDoctor.
     *
     * @param id the id of the queueForDoctorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the queueForDoctorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/queue-for-doctors/{id}")
    public ResponseEntity<QueueForDoctorDTO> getQueueForDoctor(@PathVariable Long id) {
        log.debug("REST request to get QueueForDoctor : {}", id);
        Optional<QueueForDoctorDTO> queueForDoctorDTO = queueForDoctorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(queueForDoctorDTO);
    }

    /**
     * {@code DELETE  /queue-for-doctors/:id} : delete the "id" queueForDoctor.
     *
     * @param id the id of the queueForDoctorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/queue-for-doctors/{id}")
    public ResponseEntity<Void> deleteQueueForDoctor(@PathVariable Long id) {
        log.debug("REST request to delete QueueForDoctor : {}", id);
        queueForDoctorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
