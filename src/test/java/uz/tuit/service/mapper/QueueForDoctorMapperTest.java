package uz.tuit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueueForDoctorMapperTest {

    private QueueForDoctorMapper queueForDoctorMapper;

    @BeforeEach
    public void setUp() {
        queueForDoctorMapper = new QueueForDoctorMapperImpl();
    }
}
