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
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "doctorFirstname")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentName")
    @Mapping(target = "hospital", source = "hospital", qualifiedByName = "hospitalName")
    QueueForDoctorDTO toDto(QueueForDoctor s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("doctorFirstname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstname", source = "firstname")
    DoctorDTO toDtoDoctorFirstname(Doctor doctor);

    @Named("departmentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentDTO toDtoDepartmentName(Department department);

    @Named("hospitalName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HospitalDTO toDtoHospitalName(Hospital hospital);
}
