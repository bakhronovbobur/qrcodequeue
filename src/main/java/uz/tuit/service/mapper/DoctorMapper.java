package uz.tuit.service.mapper;

import org.mapstruct.*;
import uz.tuit.domain.Doctor;
import uz.tuit.service.dto.DoctorDTO;

/**
 * Mapper for the entity {@link Doctor} and its DTO {@link DoctorDTO}.
 */
@Mapper(componentModel = "spring")
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {}
