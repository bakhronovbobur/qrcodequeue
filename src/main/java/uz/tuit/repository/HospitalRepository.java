package uz.tuit.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.tuit.domain.Hospital;

/**
 * Spring Data JPA repository for the Hospital entity.
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>, JpaSpecificationExecutor<Hospital> {
    default Optional<Hospital> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Hospital> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Hospital> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct hospital from Hospital hospital left join fetch hospital.region left join fetch hospital.district",
        countQuery = "select count(distinct hospital) from Hospital hospital"
    )
    Page<Hospital> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct hospital from Hospital hospital left join fetch hospital.region left join fetch hospital.district")
    List<Hospital> findAllWithToOneRelationships();

    @Query(
        "select hospital from Hospital hospital left join fetch hospital.region left join fetch hospital.district where hospital.id =:id"
    )
    Optional<Hospital> findOneWithToOneRelationships(@Param("id") Long id);
}
