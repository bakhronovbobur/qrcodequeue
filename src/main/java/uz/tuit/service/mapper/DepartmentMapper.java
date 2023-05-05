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
    @Mapping(target = "hospital", source = "hospital", qualifiedByName = "hospitalId")
    @Mapping(target = "doctors", source = "doctors", qualifiedByName = "doctorIdSet")
    DepartmentDTO toDto(Department s);

    @Mapping(target = "removeDoctors", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);

    @Named("hospitalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HospitalDTO toDtoHospitalId(Hospital hospital);

    @Named("doctorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoctorDTO toDtoDoctorId(Doctor doctor);

    @Named("doctorIdSet")
    default Set<DoctorDTO> toDtoDoctorIdSet(Set<Doctor> doctor) {
        return doctor.stream().map(this::toDtoDoctorId).collect(Collectors.toSet());
    }
}
