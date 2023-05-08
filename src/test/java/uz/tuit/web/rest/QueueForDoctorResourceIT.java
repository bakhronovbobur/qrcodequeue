package uz.tuit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.IntegrationTest;
import uz.tuit.domain.Department;
import uz.tuit.domain.Doctor;
import uz.tuit.domain.Hospital;
import uz.tuit.domain.QueueForDoctor;
import uz.tuit.domain.User;
import uz.tuit.repository.QueueForDoctorRepository;
import uz.tuit.service.QueueForDoctorService;
import uz.tuit.service.criteria.QueueForDoctorCriteria;
import uz.tuit.service.dto.QueueForDoctorDTO;
import uz.tuit.service.mapper.QueueForDoctorMapper;

/**
 * Integration tests for the {@link QueueForDoctorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QueueForDoctorResourceIT {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final Integer SMALLER_NUMBER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/queue-for-doctors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QueueForDoctorRepository queueForDoctorRepository;

    @Mock
    private QueueForDoctorRepository queueForDoctorRepositoryMock;

    @Autowired
    private QueueForDoctorMapper queueForDoctorMapper;

    @Mock
    private QueueForDoctorService queueForDoctorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQueueForDoctorMockMvc;

    private QueueForDoctor queueForDoctor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QueueForDoctor createEntity(EntityManager em) {
        QueueForDoctor queueForDoctor = new QueueForDoctor().number(DEFAULT_NUMBER);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        queueForDoctor.setUser(user);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        queueForDoctor.setDoctor(doctor);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        queueForDoctor.setDepartment(department);
        // Add required entity
        Hospital hospital;
        if (TestUtil.findAll(em, Hospital.class).isEmpty()) {
            hospital = HospitalResourceIT.createEntity(em);
            em.persist(hospital);
            em.flush();
        } else {
            hospital = TestUtil.findAll(em, Hospital.class).get(0);
        }
        queueForDoctor.setHospital(hospital);
        return queueForDoctor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QueueForDoctor createUpdatedEntity(EntityManager em) {
        QueueForDoctor queueForDoctor = new QueueForDoctor().number(UPDATED_NUMBER);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        queueForDoctor.setUser(user);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createUpdatedEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        queueForDoctor.setDoctor(doctor);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        queueForDoctor.setDepartment(department);
        // Add required entity
        Hospital hospital;
        if (TestUtil.findAll(em, Hospital.class).isEmpty()) {
            hospital = HospitalResourceIT.createUpdatedEntity(em);
            em.persist(hospital);
            em.flush();
        } else {
            hospital = TestUtil.findAll(em, Hospital.class).get(0);
        }
        queueForDoctor.setHospital(hospital);
        return queueForDoctor;
    }

    @BeforeEach
    public void initTest() {
        queueForDoctor = createEntity(em);
    }

    @Test
    @Transactional
    void createQueueForDoctor() throws Exception {
        int databaseSizeBeforeCreate = queueForDoctorRepository.findAll().size();
        // Create the QueueForDoctor
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(queueForDoctor);
        restQueueForDoctorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeCreate + 1);
        QueueForDoctor testQueueForDoctor = queueForDoctorList.get(queueForDoctorList.size() - 1);
        assertThat(testQueueForDoctor.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    void createQueueForDoctorWithExistingId() throws Exception {
        // Create the QueueForDoctor with an existing ID
        queueForDoctor.setId(1L);
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(queueForDoctor);

        int databaseSizeBeforeCreate = queueForDoctorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQueueForDoctorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQueueForDoctors() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get all the queueForDoctorList
        restQueueForDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(queueForDoctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQueueForDoctorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(queueForDoctorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQueueForDoctorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(queueForDoctorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQueueForDoctorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(queueForDoctorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQueueForDoctorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(queueForDoctorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQueueForDoctor() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get the queueForDoctor
        restQueueForDoctorMockMvc
            .perform(get(ENTITY_API_URL_ID, queueForDoctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(queueForDoctor.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    void getQueueForDoctorsByIdFiltering() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        Long id = queueForDoctor.getId();

        defaultQueueForDoctorShouldBeFound("id.equals=" + id);
        defaultQueueForDoctorShouldNotBeFound("id.notEquals=" + id);

        defaultQueueForDoctorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQueueForDoctorShouldNotBeFound("id.greaterThan=" + id);

        defaultQueueForDoctorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQueueForDoctorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get all the queueForDoctorList where number equals to DEFAULT_NUMBER
        defaultQueueForDoctorShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the queueForDoctorList where number equals to UPDATED_NUMBER
        defaultQueueForDoctorShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get all the queueForDoctorList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultQueueForDoctorShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the queueForDoctorList where number equals to UPDATED_NUMBER
        defaultQueueForDoctorShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get all the queueForDoctorList where number is not null
        defaultQueueForDoctorShouldBeFound("number.specified=true");

        // Get all the queueForDoctorList where number is null
        defaultQueueForDoctorShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get all the queueForDoctorList where number is greater than or equal to DEFAULT_NUMBER
        defaultQueueForDoctorShouldBeFound("number.greaterThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the queueForDoctorList where number is greater than or equal to UPDATED_NUMBER
        defaultQueueForDoctorShouldNotBeFound("number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get all the queueForDoctorList where number is less than or equal to DEFAULT_NUMBER
        defaultQueueForDoctorShouldBeFound("number.lessThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the queueForDoctorList where number is less than or equal to SMALLER_NUMBER
        defaultQueueForDoctorShouldNotBeFound("number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get all the queueForDoctorList where number is less than DEFAULT_NUMBER
        defaultQueueForDoctorShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the queueForDoctorList where number is less than UPDATED_NUMBER
        defaultQueueForDoctorShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        // Get all the queueForDoctorList where number is greater than DEFAULT_NUMBER
        defaultQueueForDoctorShouldNotBeFound("number.greaterThan=" + DEFAULT_NUMBER);

        // Get all the queueForDoctorList where number is greater than SMALLER_NUMBER
        defaultQueueForDoctorShouldBeFound("number.greaterThan=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            queueForDoctorRepository.saveAndFlush(queueForDoctor);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        queueForDoctor.setUser(user);
        queueForDoctorRepository.saveAndFlush(queueForDoctor);
        Long userId = user.getId();

        // Get all the queueForDoctorList where user equals to userId
        defaultQueueForDoctorShouldBeFound("userId.equals=" + userId);

        // Get all the queueForDoctorList where user equals to (userId + 1)
        defaultQueueForDoctorShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByDoctorIsEqualToSomething() throws Exception {
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            queueForDoctorRepository.saveAndFlush(queueForDoctor);
            doctor = DoctorResourceIT.createEntity(em);
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        em.persist(doctor);
        em.flush();
        queueForDoctor.setDoctor(doctor);
        queueForDoctorRepository.saveAndFlush(queueForDoctor);
        Long doctorId = doctor.getId();

        // Get all the queueForDoctorList where doctor equals to doctorId
        defaultQueueForDoctorShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the queueForDoctorList where doctor equals to (doctorId + 1)
        defaultQueueForDoctorShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByDepartmentIsEqualToSomething() throws Exception {
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            queueForDoctorRepository.saveAndFlush(queueForDoctor);
            department = DepartmentResourceIT.createEntity(em);
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(department);
        em.flush();
        queueForDoctor.setDepartment(department);
        queueForDoctorRepository.saveAndFlush(queueForDoctor);
        Long departmentId = department.getId();

        // Get all the queueForDoctorList where department equals to departmentId
        defaultQueueForDoctorShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the queueForDoctorList where department equals to (departmentId + 1)
        defaultQueueForDoctorShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllQueueForDoctorsByHospitalIsEqualToSomething() throws Exception {
        Hospital hospital;
        if (TestUtil.findAll(em, Hospital.class).isEmpty()) {
            queueForDoctorRepository.saveAndFlush(queueForDoctor);
            hospital = HospitalResourceIT.createEntity(em);
        } else {
            hospital = TestUtil.findAll(em, Hospital.class).get(0);
        }
        em.persist(hospital);
        em.flush();
        queueForDoctor.setHospital(hospital);
        queueForDoctorRepository.saveAndFlush(queueForDoctor);
        Long hospitalId = hospital.getId();

        // Get all the queueForDoctorList where hospital equals to hospitalId
        defaultQueueForDoctorShouldBeFound("hospitalId.equals=" + hospitalId);

        // Get all the queueForDoctorList where hospital equals to (hospitalId + 1)
        defaultQueueForDoctorShouldNotBeFound("hospitalId.equals=" + (hospitalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQueueForDoctorShouldBeFound(String filter) throws Exception {
        restQueueForDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(queueForDoctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));

        // Check, that the count call also returns 1
        restQueueForDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQueueForDoctorShouldNotBeFound(String filter) throws Exception {
        restQueueForDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQueueForDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQueueForDoctor() throws Exception {
        // Get the queueForDoctor
        restQueueForDoctorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQueueForDoctor() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();

        // Update the queueForDoctor
        QueueForDoctor updatedQueueForDoctor = queueForDoctorRepository.findById(queueForDoctor.getId()).get();
        // Disconnect from session so that the updates on updatedQueueForDoctor are not directly saved in db
        em.detach(updatedQueueForDoctor);
        updatedQueueForDoctor.number(UPDATED_NUMBER);
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(updatedQueueForDoctor);

        restQueueForDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queueForDoctorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isOk());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
        QueueForDoctor testQueueForDoctor = queueForDoctorList.get(queueForDoctorList.size() - 1);
        assertThat(testQueueForDoctor.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingQueueForDoctor() throws Exception {
        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();
        queueForDoctor.setId(count.incrementAndGet());

        // Create the QueueForDoctor
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(queueForDoctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueueForDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queueForDoctorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQueueForDoctor() throws Exception {
        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();
        queueForDoctor.setId(count.incrementAndGet());

        // Create the QueueForDoctor
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(queueForDoctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueForDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQueueForDoctor() throws Exception {
        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();
        queueForDoctor.setId(count.incrementAndGet());

        // Create the QueueForDoctor
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(queueForDoctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueForDoctorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQueueForDoctorWithPatch() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();

        // Update the queueForDoctor using partial update
        QueueForDoctor partialUpdatedQueueForDoctor = new QueueForDoctor();
        partialUpdatedQueueForDoctor.setId(queueForDoctor.getId());

        partialUpdatedQueueForDoctor.number(UPDATED_NUMBER);

        restQueueForDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueueForDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQueueForDoctor))
            )
            .andExpect(status().isOk());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
        QueueForDoctor testQueueForDoctor = queueForDoctorList.get(queueForDoctorList.size() - 1);
        assertThat(testQueueForDoctor.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateQueueForDoctorWithPatch() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();

        // Update the queueForDoctor using partial update
        QueueForDoctor partialUpdatedQueueForDoctor = new QueueForDoctor();
        partialUpdatedQueueForDoctor.setId(queueForDoctor.getId());

        partialUpdatedQueueForDoctor.number(UPDATED_NUMBER);

        restQueueForDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueueForDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQueueForDoctor))
            )
            .andExpect(status().isOk());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
        QueueForDoctor testQueueForDoctor = queueForDoctorList.get(queueForDoctorList.size() - 1);
        assertThat(testQueueForDoctor.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingQueueForDoctor() throws Exception {
        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();
        queueForDoctor.setId(count.incrementAndGet());

        // Create the QueueForDoctor
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(queueForDoctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueueForDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, queueForDoctorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQueueForDoctor() throws Exception {
        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();
        queueForDoctor.setId(count.incrementAndGet());

        // Create the QueueForDoctor
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(queueForDoctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueForDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQueueForDoctor() throws Exception {
        int databaseSizeBeforeUpdate = queueForDoctorRepository.findAll().size();
        queueForDoctor.setId(count.incrementAndGet());

        // Create the QueueForDoctor
        QueueForDoctorDTO queueForDoctorDTO = queueForDoctorMapper.toDto(queueForDoctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueForDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(queueForDoctorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QueueForDoctor in the database
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQueueForDoctor() throws Exception {
        // Initialize the database
        queueForDoctorRepository.saveAndFlush(queueForDoctor);

        int databaseSizeBeforeDelete = queueForDoctorRepository.findAll().size();

        // Delete the queueForDoctor
        restQueueForDoctorMockMvc
            .perform(delete(ENTITY_API_URL_ID, queueForDoctor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QueueForDoctor> queueForDoctorList = queueForDoctorRepository.findAll();
        assertThat(queueForDoctorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
