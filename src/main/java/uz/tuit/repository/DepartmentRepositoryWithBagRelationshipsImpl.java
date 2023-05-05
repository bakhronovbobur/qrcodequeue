package uz.tuit.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uz.tuit.domain.Department;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DepartmentRepositoryWithBagRelationshipsImpl implements DepartmentRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Department> fetchBagRelationships(Optional<Department> department) {
        return department.map(this::fetchDoctors);
    }

    @Override
    public Page<Department> fetchBagRelationships(Page<Department> departments) {
        return new PageImpl<>(fetchBagRelationships(departments.getContent()), departments.getPageable(), departments.getTotalElements());
    }

    @Override
    public List<Department> fetchBagRelationships(List<Department> departments) {
        return Optional.of(departments).map(this::fetchDoctors).orElse(Collections.emptyList());
    }

    Department fetchDoctors(Department result) {
        return entityManager
            .createQuery(
                "select department from Department department left join fetch department.doctors where department is :department",
                Department.class
            )
            .setParameter("department", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Department> fetchDoctors(List<Department> departments) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, departments.size()).forEach(index -> order.put(departments.get(index).getId(), index));
        List<Department> result = entityManager
            .createQuery(
                "select distinct department from Department department left join fetch department.doctors where department in :departments",
                Department.class
            )
            .setParameter("departments", departments)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
