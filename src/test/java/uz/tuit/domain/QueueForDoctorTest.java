package uz.tuit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tuit.web.rest.TestUtil;

class QueueForDoctorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QueueForDoctor.class);
        QueueForDoctor queueForDoctor1 = new QueueForDoctor();
        queueForDoctor1.setId(1L);
        QueueForDoctor queueForDoctor2 = new QueueForDoctor();
        queueForDoctor2.setId(queueForDoctor1.getId());
        assertThat(queueForDoctor1).isEqualTo(queueForDoctor2);
        queueForDoctor2.setId(2L);
        assertThat(queueForDoctor1).isNotEqualTo(queueForDoctor2);
        queueForDoctor1.setId(null);
        assertThat(queueForDoctor1).isNotEqualTo(queueForDoctor2);
    }
}
