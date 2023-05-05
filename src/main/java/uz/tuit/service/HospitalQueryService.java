package uz.tuit.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.tuit.domain.*; // for static metamodels
import uz.tuit.domain.Hospital;
import uz.tuit.repository.HospitalRepository;
import uz.tuit.service.criteria.HospitalCriteria;
import uz.tuit.service.dto.HospitalDTO;
import uz.tuit.service.mapper.HospitalMapper;

/**
 * Service for executing complex queries for {@link Hospital} entities in the database.
 * The main input is a {@link HospitalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HospitalDTO} or a {@link Page} of {@link HospitalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HospitalQueryService extends QueryService<Hospital> {

    private final Logger log = LoggerFactory.getLogger(HospitalQueryService.class);

    private final HospitalRepository hospitalRepository;

    private final HospitalMapper hospitalMapper;

    public HospitalQueryService(HospitalRepository hospitalRepository, HospitalMapper hospitalMapper) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalMapper = hospitalMapper;
    }

    /**
     * Return a {@link List} of {@link HospitalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HospitalDTO> findByCriteria(HospitalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalMapper.toDto(hospitalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HospitalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HospitalDTO> findByCriteria(HospitalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalRepository.findAll(specification, page).map(hospitalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HospitalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalRepository.count(specification);
    }

    /**
     * Function to convert {@link HospitalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Hospital> createSpecification(HospitalCriteria criteria) {
        Specification<Hospital> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Hospital_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Hospital_.name));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Hospital_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Hospital_.latitude));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Hospital_.description));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Hospital_.address));
            }
            if (criteria.getRegionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRegionId(), root -> root.join(Hospital_.region, JoinType.LEFT).get(Region_.id))
                    );
            }
            if (criteria.getDistrictId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDistrictId(), root -> root.join(Hospital_.district, JoinType.LEFT).get(District_.id))
                    );
            }
            if (criteria.getDepartmentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentsId(),
                            root -> root.join(Hospital_.departments, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
