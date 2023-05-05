package uz.tuit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.IntegrationTest;
import uz.tuit.domain.Department;
import uz.tuit.domain.Doctor;
import uz.tuit.repository.DoctorRepository;
import uz.tuit.service.criteria.DoctorCriteria;
import uz.tuit.service.dto.DoctorDTO;
import uz.tuit.service.mapper.DoctorMapper;

/**
 * Integration tests for the {@link DoctorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DoctorResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doctors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .phone(DEFAULT_PHONE)
            .position(DEFAULT_POSITION)
            .qualification(DEFAULT_QUALIFICATION);
        return doctor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createUpdatedEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .phone(UPDATED_PHONE)
            .position(UPDATED_POSITION)
            .qualification(UPDATED_QUALIFICATION);
        return doctor;
    }

    @BeforeEach
    public void initTest() {
        doctor = createEntity(em);
    }

    @Test
    @Transactional
    void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();
        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);
        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testDoctor.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testDoctor.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testDoctor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDoctor.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testDoctor.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
    }

    @Test
    @Transactional
    void createDoctorWithExistingId() throws Exception {
        // Create the Doctor with an existing ID
        doctor.setId(1L);
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setFirstname(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setLastname(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMiddleNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setMiddleName(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)));
    }

    @Test
    @Transactional
    void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL_ID, doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION));
    }

    @Test
    @Transactional
    void getDoctorsByIdFiltering() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        Long id = doctor.getId();

        defaultDoctorShouldBeFound("id.equals=" + id);
        defaultDoctorShouldNotBeFound("id.notEquals=" + id);

        defaultDoctorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDoctorShouldNotBeFound("id.greaterThan=" + id);

        defaultDoctorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDoctorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstname equals to DEFAULT_FIRSTNAME
        defaultDoctorShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the doctorList where firstname equals to UPDATED_FIRSTNAME
        defaultDoctorShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultDoctorShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the doctorList where firstname equals to UPDATED_FIRSTNAME
        defaultDoctorShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstname is not null
        defaultDoctorShouldBeFound("firstname.specified=true");

        // Get all the doctorList where firstname is null
        defaultDoctorShouldNotBeFound("firstname.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstname contains DEFAULT_FIRSTNAME
        defaultDoctorShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the doctorList where firstname contains UPDATED_FIRSTNAME
        defaultDoctorShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstname does not contain DEFAULT_FIRSTNAME
        defaultDoctorShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the doctorList where firstname does not contain UPDATED_FIRSTNAME
        defaultDoctorShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastname equals to DEFAULT_LASTNAME
        defaultDoctorShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

        // Get all the doctorList where lastname equals to UPDATED_LASTNAME
        defaultDoctorShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastname in DEFAULT_LASTNAME or UPDATED_LASTNAME
        defaultDoctorShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

        // Get all the doctorList where lastname equals to UPDATED_LASTNAME
        defaultDoctorShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastname is not null
        defaultDoctorShouldBeFound("lastname.specified=true");

        // Get all the doctorList where lastname is null
        defaultDoctorShouldNotBeFound("lastname.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByLastnameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastname contains DEFAULT_LASTNAME
        defaultDoctorShouldBeFound("lastname.contains=" + DEFAULT_LASTNAME);

        // Get all the doctorList where lastname contains UPDATED_LASTNAME
        defaultDoctorShouldNotBeFound("lastname.contains=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastnameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastname does not contain DEFAULT_LASTNAME
        defaultDoctorShouldNotBeFound("lastname.doesNotContain=" + DEFAULT_LASTNAME);

        // Get all the doctorList where lastname does not contain UPDATED_LASTNAME
        defaultDoctorShouldBeFound("lastname.doesNotContain=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultDoctorShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the doctorList where middleName equals to UPDATED_MIDDLE_NAME
        defaultDoctorShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultDoctorShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the doctorList where middleName equals to UPDATED_MIDDLE_NAME
        defaultDoctorShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where middleName is not null
        defaultDoctorShouldBeFound("middleName.specified=true");

        // Get all the doctorList where middleName is null
        defaultDoctorShouldNotBeFound("middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where middleName contains DEFAULT_MIDDLE_NAME
        defaultDoctorShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the doctorList where middleName contains UPDATED_MIDDLE_NAME
        defaultDoctorShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultDoctorShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the doctorList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultDoctorShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone equals to DEFAULT_PHONE
        defaultDoctorShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the doctorList where phone equals to UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the doctorList where phone equals to UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone is not null
        defaultDoctorShouldBeFound("phone.specified=true");

        // Get all the doctorList where phone is null
        defaultDoctorShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone contains DEFAULT_PHONE
        defaultDoctorShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the doctorList where phone contains UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone does not contain DEFAULT_PHONE
        defaultDoctorShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the doctorList where phone does not contain UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where position equals to DEFAULT_POSITION
        defaultDoctorShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the doctorList where position equals to UPDATED_POSITION
        defaultDoctorShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDoctorsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultDoctorShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the doctorList where position equals to UPDATED_POSITION
        defaultDoctorShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDoctorsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where position is not null
        defaultDoctorShouldBeFound("position.specified=true");

        // Get all the doctorList where position is null
        defaultDoctorShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByPositionContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where position contains DEFAULT_POSITION
        defaultDoctorShouldBeFound("position.contains=" + DEFAULT_POSITION);

        // Get all the doctorList where position contains UPDATED_POSITION
        defaultDoctorShouldNotBeFound("position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDoctorsByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where position does not contain DEFAULT_POSITION
        defaultDoctorShouldNotBeFound("position.doesNotContain=" + DEFAULT_POSITION);

        // Get all the doctorList where position does not contain UPDATED_POSITION
        defaultDoctorShouldBeFound("position.doesNotContain=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDoctorsByQualificationIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where qualification equals to DEFAULT_QUALIFICATION
        defaultDoctorShouldBeFound("qualification.equals=" + DEFAULT_QUALIFICATION);

        // Get all the doctorList where qualification equals to UPDATED_QUALIFICATION
        defaultDoctorShouldNotBeFound("qualification.equals=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllDoctorsByQualificationIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where qualification in DEFAULT_QUALIFICATION or UPDATED_QUALIFICATION
        defaultDoctorShouldBeFound("qualification.in=" + DEFAULT_QUALIFICATION + "," + UPDATED_QUALIFICATION);

        // Get all the doctorList where qualification equals to UPDATED_QUALIFICATION
        defaultDoctorShouldNotBeFound("qualification.in=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllDoctorsByQualificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where qualification is not null
        defaultDoctorShouldBeFound("qualification.specified=true");

        // Get all the doctorList where qualification is null
        defaultDoctorShouldNotBeFound("qualification.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByQualificationContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where qualification contains DEFAULT_QUALIFICATION
        defaultDoctorShouldBeFound("qualification.contains=" + DEFAULT_QUALIFICATION);

        // Get all the doctorList where qualification contains UPDATED_QUALIFICATION
        defaultDoctorShouldNotBeFound("qualification.contains=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllDoctorsByQualificationNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where qualification does not contain DEFAULT_QUALIFICATION
        defaultDoctorShouldNotBeFound("qualification.doesNotContain=" + DEFAULT_QUALIFICATION);

        // Get all the doctorList where qualification does not contain UPDATED_QUALIFICATION
        defaultDoctorShouldBeFound("qualification.doesNotContain=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void getAllDoctorsByDepartmentsIsEqualToSomething() throws Exception {
        Department departments;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            doctorRepository.saveAndFlush(doctor);
            departments = DepartmentResourceIT.createEntity(em);
        } else {
            departments = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(departments);
        em.flush();
        doctor.addDepartments(departments);
        doctorRepository.saveAndFlush(doctor);
        Long departmentsId = departments.getId();

        // Get all the doctorList where departments equals to departmentsId
        defaultDoctorShouldBeFound("departmentsId.equals=" + departmentsId);

        // Get all the doctorList where departments equals to (departmentsId + 1)
        defaultDoctorShouldNotBeFound("departmentsId.equals=" + (departmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorShouldBeFound(String filter) throws Exception {
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)));

        // Check, that the count call also returns 1
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorShouldNotBeFound(String filter) throws Exception {
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).get();
        // Disconnect from session so that the updates on updatedDoctor are not directly saved in db
        em.detach(updatedDoctor);
        updatedDoctor
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .phone(UPDATED_PHONE)
            .position(UPDATED_POSITION)
            .qualification(UPDATED_QUALIFICATION);
        DoctorDTO doctorDTO = doctorMapper.toDto(updatedDoctor);

        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testDoctor.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testDoctor.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testDoctor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDoctor.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testDoctor.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void putNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoctorWithPatch() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor using partial update
        Doctor partialUpdatedDoctor = new Doctor();
        partialUpdatedDoctor.setId(doctor.getId());

        partialUpdatedDoctor.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).position(UPDATED_POSITION);

        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoctor))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testDoctor.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testDoctor.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testDoctor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDoctor.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testDoctor.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
    }

    @Test
    @Transactional
    void fullUpdateDoctorWithPatch() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor using partial update
        Doctor partialUpdatedDoctor = new Doctor();
        partialUpdatedDoctor.setId(doctor.getId());

        partialUpdatedDoctor
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .phone(UPDATED_PHONE)
            .position(UPDATED_POSITION)
            .qualification(UPDATED_QUALIFICATION);

        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoctor))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testDoctor.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testDoctor.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testDoctor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDoctor.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testDoctor.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    void patchNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doctorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Delete the doctor
        restDoctorMockMvc
            .perform(delete(ENTITY_API_URL_ID, doctor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
