package uz.tuit.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.tuit.domain.QueueForDoctor;

/**
 * Spring Data JPA repository for the QueueForDoctor entity.
 */
@Repository
public interface QueueForDoctorRepository extends JpaRepository<QueueForDoctor, Long>, JpaSpecificationExecutor<QueueForDoctor> {
    @Query("select queueForDoctor from QueueForDoctor queueForDoctor where queueForDoctor.user.login = ?#{principal.username}")
    List<QueueForDoctor> findByUserIsCurrentUser();

    default Optional<QueueForDoctor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QueueForDoctor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QueueForDoctor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct queueForDoctor from QueueForDoctor queueForDoctor left join fetch queueForDoctor.user left join fetch queueForDoctor.doctor left join fetch queueForDoctor.department left join fetch queueForDoctor.hospital",
        countQuery = "select count(distinct queueForDoctor) from QueueForDoctor queueForDoctor"
    )
    Page<QueueForDoctor> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct queueForDoctor from QueueForDoctor queueForDoctor left join fetch queueForDoctor.user left join fetch queueForDoctor.doctor left join fetch queueForDoctor.department left join fetch queueForDoctor.hospital"
    )
    List<QueueForDoctor> findAllWithToOneRelationships();

    @Query(
        "select queueForDoctor from QueueForDoctor queueForDoctor left join fetch queueForDoctor.user left join fetch queueForDoctor.doctor left join fetch queueForDoctor.department left join fetch queueForDoctor.hospital where queueForDoctor.id =:id"
    )
    Optional<QueueForDoctor> findOneWithToOneRelationships(@Param("id") Long id);
}
