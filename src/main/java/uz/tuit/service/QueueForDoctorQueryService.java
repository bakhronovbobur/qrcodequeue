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
import uz.tuit.domain.QueueForDoctor;
import uz.tuit.repository.QueueForDoctorRepository;
import uz.tuit.service.criteria.QueueForDoctorCriteria;
import uz.tuit.service.dto.QueueForDoctorDTO;
import uz.tuit.service.mapper.QueueForDoctorMapper;

/**
 * Service for executing complex queries for {@link QueueForDoctor} entities in the database.
 * The main input is a {@link QueueForDoctorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QueueForDoctorDTO} or a {@link Page} of {@link QueueForDoctorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QueueForDoctorQueryService extends QueryService<QueueForDoctor> {

    private final Logger log = LoggerFactory.getLogger(QueueForDoctorQueryService.class);

    private final QueueForDoctorRepository queueForDoctorRepository;

    private final QueueForDoctorMapper queueForDoctorMapper;

    public QueueForDoctorQueryService(QueueForDoctorRepository queueForDoctorRepository, QueueForDoctorMapper queueForDoctorMapper) {
        this.queueForDoctorRepository = queueForDoctorRepository;
        this.queueForDoctorMapper = queueForDoctorMapper;
    }

    /**
     * Return a {@link List} of {@link QueueForDoctorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QueueForDoctorDTO> findByCriteria(QueueForDoctorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QueueForDoctor> specification = createSpecification(criteria);
        return queueForDoctorMapper.toDto(queueForDoctorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QueueForDoctorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QueueForDoctorDTO> findByCriteria(QueueForDoctorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QueueForDoctor> specification = createSpecification(criteria);
        return queueForDoctorRepository.findAll(specification, page).map(queueForDoctorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QueueForDoctorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QueueForDoctor> specification = createSpecification(criteria);
        return queueForDoctorRepository.count(specification);
    }

    /**
     * Function to convert {@link QueueForDoctorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QueueForDoctor> createSpecification(QueueForDoctorCriteria criteria) {
        Specification<QueueForDoctor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QueueForDoctor_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), QueueForDoctor_.number));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(QueueForDoctor_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getDoctorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDoctorId(), root -> root.join(QueueForDoctor_.doctor, JoinType.LEFT).get(Doctor_.id))
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(QueueForDoctor_.department, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
            if (criteria.getHospitalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHospitalId(),
                            root -> root.join(QueueForDoctor_.hospital, JoinType.LEFT).get(Hospital_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
