package uz.tuit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.tuit.domain.QueueForDoctor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QueueForDoctorDTO implements Serializable {

    private Long id;

    private Integer number;

    private UserDTO user;

    private DoctorDTO doctor;

    private DepartmentDTO department;

    private HospitalDTO hospital;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public HospitalDTO getHospital() {
        return hospital;
    }

    public void setHospital(HospitalDTO hospital) {
        this.hospital = hospital;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueueForDoctorDTO)) {
            return false;
        }

        QueueForDoctorDTO queueForDoctorDTO = (QueueForDoctorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, queueForDoctorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QueueForDoctorDTO{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", user=" + getUser() +
            ", doctor=" + getDoctor() +
            ", department=" + getDepartment() +
            ", hospital=" + getHospital() +
            "}";
    }
}
