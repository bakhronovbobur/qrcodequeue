package uz.tuit.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tuit.domain.QueueForDoctor;

/**
 * Spring Data JPA repository for the QueueForDoctor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueueForDoctorRepository extends JpaRepository<QueueForDoctor, Long>, JpaSpecificationExecutor<QueueForDoctor> {
    @Query("select queueForDoctor from QueueForDoctor queueForDoctor where queueForDoctor.user.login = ?#{principal.username}")
    List<QueueForDoctor> findByUserIsCurrentUser();
}
