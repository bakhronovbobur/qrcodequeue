package uz.tuit.service.mapper;

import org.mapstruct.*;
import uz.tuit.domain.Department;
import uz.tuit.domain.Doctor;
import uz.tuit.domain.Hospital;
import uz.tuit.domain.QueueForDoctor;
import uz.tuit.domain.User;
import uz.tuit.service.dto.DepartmentDTO;
import uz.tuit.service.dto.DoctorDTO;
import uz.tuit.service.dto.HospitalDTO;
import uz.tuit.service.dto.QueueForDoctorDTO;
import uz.tuit.service.dto.UserDTO;

/**
 * Mapper for the entity {@link QueueForDoctor} and its DTO {@link QueueForDoctorDTO}.
 */
@Mapper(componentModel = "spring")
public interface QueueForDoctorMapper extends EntityMapper<QueueForDoctorDTO, QueueForDoctor> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "doctorId")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    @Mapping(target = "hospital", source = "hospital", qualifiedByName = "hospitalId")
    QueueForDoctorDTO toDto(QueueForDoctor s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("doctorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoctorDTO toDtoDoctorId(Doctor doctor);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);

    @Named("hospitalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HospitalDTO toDtoHospitalId(Hospital hospital);
}
