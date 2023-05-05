package uz.tuit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tuit.web.rest.TestUtil;

class QueueForDoctorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QueueForDoctorDTO.class);
        QueueForDoctorDTO queueForDoctorDTO1 = new QueueForDoctorDTO();
        queueForDoctorDTO1.setId(1L);
        QueueForDoctorDTO queueForDoctorDTO2 = new QueueForDoctorDTO();
        assertThat(queueForDoctorDTO1).isNotEqualTo(queueForDoctorDTO2);
        queueForDoctorDTO2.setId(queueForDoctorDTO1.getId());
        assertThat(queueForDoctorDTO1).isEqualTo(queueForDoctorDTO2);
        queueForDoctorDTO2.setId(2L);
        assertThat(queueForDoctorDTO1).isNotEqualTo(queueForDoctorDTO2);
        queueForDoctorDTO1.setId(null);
        assertThat(queueForDoctorDTO1).isNotEqualTo(queueForDoctorDTO2);
    }
}
