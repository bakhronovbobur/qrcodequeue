package uz.tuit.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import uz.tuit.domain.Department;
import uz.tuit.domain.Doctor;
import uz.tuit.domain.Hospital;
import uz.tuit.service.dto.DepartmentDTO;
import uz.tuit.service.dto.DoctorDTO;
import uz.tuit.service.dto.HospitalDTO;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(target = "hospital", source = "hospital", qualifiedByName = "hospitalName")
    @Mapping(target = "doctors", source = "doctors", qualifiedByName = "doctorFirstnameSet")
    DepartmentDTO toDto(Department s);

    @Mapping(target = "removeDoctors", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);

    @Named("hospitalName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HospitalDTO toDtoHospitalName(Hospital hospital);

    @Named("doctorFirstname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstname", source = "firstname")
    DoctorDTO toDtoDoctorFirstname(Doctor doctor);

    @Named("doctorFirstnameSet")
    default Set<DoctorDTO> toDtoDoctorFirstnameSet(Set<Doctor> doctor) {
        return doctor.stream().map(this::toDtoDoctorFirstname).collect(Collectors.toSet());
    }
}
