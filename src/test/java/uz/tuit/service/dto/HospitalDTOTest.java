package uz.tuit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tuit.web.rest.TestUtil;

class HospitalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HospitalDTO.class);
        HospitalDTO hospitalDTO1 = new HospitalDTO();
        hospitalDTO1.setId(1L);
        HospitalDTO hospitalDTO2 = new HospitalDTO();
        assertThat(hospitalDTO1).isNotEqualTo(hospitalDTO2);
        hospitalDTO2.setId(hospitalDTO1.getId());
        assertThat(hospitalDTO1).isEqualTo(hospitalDTO2);
        hospitalDTO2.setId(2L);
        assertThat(hospitalDTO1).isNotEqualTo(hospitalDTO2);
        hospitalDTO1.setId(null);
        assertThat(hospitalDTO1).isNotEqualTo(hospitalDTO2);
    }
}
