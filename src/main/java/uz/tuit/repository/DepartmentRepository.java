package uz.tuit.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.tuit.domain.Department;

/**
 * Spring Data JPA repository for the Department entity.
 *
 * When extending this class, extend DepartmentRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DepartmentRepository
    extends DepartmentRepositoryWithBagRelationships, JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    default Optional<Department> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Department> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Department> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct department from Department department left join fetch department.hospital",
        countQuery = "select count(distinct department) from Department department"
    )
    Page<Department> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct department from Department department left join fetch department.hospital")
    List<Department> findAllWithToOneRelationships();

    @Query("select department from Department department left join fetch department.hospital where department.id =:id")
    Optional<Department> findOneWithToOneRelationships(@Param("id") Long id);
}
