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
import uz.tuit.domain.District;
import uz.tuit.domain.Hospital;
import uz.tuit.domain.Region;
import uz.tuit.repository.HospitalRepository;
import uz.tuit.service.HospitalService;
import uz.tuit.service.criteria.HospitalCriteria;
import uz.tuit.service.dto.HospitalDTO;
import uz.tuit.service.mapper.HospitalMapper;

/**
 * Integration tests for the {@link HospitalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HospitalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hospitals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HospitalRepository hospitalRepository;

    @Mock
    private HospitalRepository hospitalRepositoryMock;

    @Autowired
    private HospitalMapper hospitalMapper;

    @Mock
    private HospitalService hospitalServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHospitalMockMvc;

    private Hospital hospital;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(DEFAULT_NAME)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .description(DEFAULT_DESCRIPTION)
            .address(DEFAULT_ADDRESS);
        // Add required entity
        Region region;
        if (TestUtil.findAll(em, Region.class).isEmpty()) {
            region = RegionResourceIT.createEntity(em);
            em.persist(region);
            em.flush();
        } else {
            region = TestUtil.findAll(em, Region.class).get(0);
        }
        hospital.setRegion(region);
        // Add required entity
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        hospital.setDistrict(district);
        return hospital;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createUpdatedEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(UPDATED_NAME)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS);
        // Add required entity
        Region region;
        if (TestUtil.findAll(em, Region.class).isEmpty()) {
            region = RegionResourceIT.createUpdatedEntity(em);
            em.persist(region);
            em.flush();
        } else {
            region = TestUtil.findAll(em, Region.class).get(0);
        }
        hospital.setRegion(region);
        // Add required entity
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createUpdatedEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        hospital.setDistrict(district);
        return hospital;
    }

    @BeforeEach
    public void initTest() {
        hospital = createEntity(em);
    }

    @Test
    @Transactional
    void createHospital() throws Exception {
        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();
        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);
        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isCreated());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate + 1);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHospital.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testHospital.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testHospital.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHospital.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createHospitalWithExistingId() throws Exception {
        // Create the Hospital with an existing ID
        hospital.setId(1L);
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalRepository.findAll().size();
        // set the field null
        hospital.setName(null);

        // Create the Hospital, which fails.
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isBadRequest());

        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHospitals() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHospitalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(hospitalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHospitalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(hospitalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHospitalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(hospitalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHospitalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(hospitalRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get the hospital
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL_ID, hospital.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hospital.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getHospitalsByIdFiltering() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        Long id = hospital.getId();

        defaultHospitalShouldBeFound("id.equals=" + id);
        defaultHospitalShouldNotBeFound("id.notEquals=" + id);

        defaultHospitalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHospitalShouldNotBeFound("id.greaterThan=" + id);

        defaultHospitalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHospitalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name equals to DEFAULT_NAME
        defaultHospitalShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the hospitalList where name equals to UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHospitalShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the hospitalList where name equals to UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name is not null
        defaultHospitalShouldBeFound("name.specified=true");

        // Get all the hospitalList where name is null
        defaultHospitalShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByNameContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name contains DEFAULT_NAME
        defaultHospitalShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the hospitalList where name contains UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name does not contain DEFAULT_NAME
        defaultHospitalShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the hospitalList where name does not contain UPDATED_NAME
        defaultHospitalShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where longitude equals to DEFAULT_LONGITUDE
        defaultHospitalShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the hospitalList where longitude equals to UPDATED_LONGITUDE
        defaultHospitalShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultHospitalShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the hospitalList where longitude equals to UPDATED_LONGITUDE
        defaultHospitalShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where longitude is not null
        defaultHospitalShouldBeFound("longitude.specified=true");

        // Get all the hospitalList where longitude is null
        defaultHospitalShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultHospitalShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the hospitalList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultHospitalShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultHospitalShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the hospitalList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultHospitalShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where longitude is less than DEFAULT_LONGITUDE
        defaultHospitalShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the hospitalList where longitude is less than UPDATED_LONGITUDE
        defaultHospitalShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where longitude is greater than DEFAULT_LONGITUDE
        defaultHospitalShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the hospitalList where longitude is greater than SMALLER_LONGITUDE
        defaultHospitalShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where latitude equals to DEFAULT_LATITUDE
        defaultHospitalShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the hospitalList where latitude equals to UPDATED_LATITUDE
        defaultHospitalShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultHospitalShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the hospitalList where latitude equals to UPDATED_LATITUDE
        defaultHospitalShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where latitude is not null
        defaultHospitalShouldBeFound("latitude.specified=true");

        // Get all the hospitalList where latitude is null
        defaultHospitalShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultHospitalShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the hospitalList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultHospitalShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultHospitalShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the hospitalList where latitude is less than or equal to SMALLER_LATITUDE
        defaultHospitalShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where latitude is less than DEFAULT_LATITUDE
        defaultHospitalShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the hospitalList where latitude is less than UPDATED_LATITUDE
        defaultHospitalShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where latitude is greater than DEFAULT_LATITUDE
        defaultHospitalShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the hospitalList where latitude is greater than SMALLER_LATITUDE
        defaultHospitalShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHospitalsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where description equals to DEFAULT_DESCRIPTION
        defaultHospitalShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the hospitalList where description equals to UPDATED_DESCRIPTION
        defaultHospitalShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHospitalsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultHospitalShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the hospitalList where description equals to UPDATED_DESCRIPTION
        defaultHospitalShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHospitalsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where description is not null
        defaultHospitalShouldBeFound("description.specified=true");

        // Get all the hospitalList where description is null
        defaultHospitalShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where description contains DEFAULT_DESCRIPTION
        defaultHospitalShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the hospitalList where description contains UPDATED_DESCRIPTION
        defaultHospitalShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHospitalsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where description does not contain DEFAULT_DESCRIPTION
        defaultHospitalShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the hospitalList where description does not contain UPDATED_DESCRIPTION
        defaultHospitalShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address equals to DEFAULT_ADDRESS
        defaultHospitalShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address equals to UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultHospitalShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the hospitalList where address equals to UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address is not null
        defaultHospitalShouldBeFound("address.specified=true");

        // Get all the hospitalList where address is null
        defaultHospitalShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address contains DEFAULT_ADDRESS
        defaultHospitalShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address contains UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address does not contain DEFAULT_ADDRESS
        defaultHospitalShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address does not contain UPDATED_ADDRESS
        defaultHospitalShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByRegionIsEqualToSomething() throws Exception {
        Region region;
        if (TestUtil.findAll(em, Region.class).isEmpty()) {
            hospitalRepository.saveAndFlush(hospital);
            region = RegionResourceIT.createEntity(em);
        } else {
            region = TestUtil.findAll(em, Region.class).get(0);
        }
        em.persist(region);
        em.flush();
        hospital.setRegion(region);
        hospitalRepository.saveAndFlush(hospital);
        Long regionId = region.getId();

        // Get all the hospitalList where region equals to regionId
        defaultHospitalShouldBeFound("regionId.equals=" + regionId);

        // Get all the hospitalList where region equals to (regionId + 1)
        defaultHospitalShouldNotBeFound("regionId.equals=" + (regionId + 1));
    }

    @Test
    @Transactional
    void getAllHospitalsByDistrictIsEqualToSomething() throws Exception {
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            hospitalRepository.saveAndFlush(hospital);
            district = DistrictResourceIT.createEntity(em);
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(district);
        em.flush();
        hospital.setDistrict(district);
        hospitalRepository.saveAndFlush(hospital);
        Long districtId = district.getId();

        // Get all the hospitalList where district equals to districtId
        defaultHospitalShouldBeFound("districtId.equals=" + districtId);

        // Get all the hospitalList where district equals to (districtId + 1)
        defaultHospitalShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    @Test
    @Transactional
    void getAllHospitalsByDepartmentsIsEqualToSomething() throws Exception {
        Department departments;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            hospitalRepository.saveAndFlush(hospital);
            departments = DepartmentResourceIT.createEntity(em);
        } else {
            departments = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(departments);
        em.flush();
        hospital.addDepartments(departments);
        hospitalRepository.saveAndFlush(hospital);
        Long departmentsId = departments.getId();

        // Get all the hospitalList where departments equals to departmentsId
        defaultHospitalShouldBeFound("departmentsId.equals=" + departmentsId);

        // Get all the hospitalList where departments equals to (departmentsId + 1)
        defaultHospitalShouldNotBeFound("departmentsId.equals=" + (departmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHospitalShouldBeFound(String filter) throws Exception {
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHospitalShouldNotBeFound(String filter) throws Exception {
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHospital() throws Exception {
        // Get the hospital
        restHospitalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital
        Hospital updatedHospital = hospitalRepository.findById(hospital.getId()).get();
        // Disconnect from session so that the updates on updatedHospital are not directly saved in db
        em.detach(updatedHospital);
        updatedHospital
            .name(UPDATED_NAME)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS);
        HospitalDTO hospitalDTO = hospitalMapper.toDto(updatedHospital);

        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hospitalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHospital.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testHospital.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testHospital.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHospital.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hospitalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHospitalWithPatch() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital using partial update
        Hospital partialUpdatedHospital = new Hospital();
        partialUpdatedHospital.setId(hospital.getId());

        partialUpdatedHospital.address(UPDATED_ADDRESS);

        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHospital))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHospital.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testHospital.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testHospital.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHospital.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateHospitalWithPatch() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital using partial update
        Hospital partialUpdatedHospital = new Hospital();
        partialUpdatedHospital.setId(hospital.getId());

        partialUpdatedHospital
            .name(UPDATED_NAME)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS);

        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHospital))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHospital.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testHospital.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testHospital.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHospital.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hospitalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeDelete = hospitalRepository.findAll().size();

        // Delete the hospital
        restHospitalMockMvc
            .perform(delete(ENTITY_API_URL_ID, hospital.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
