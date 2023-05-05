package uz.tuit.service.mapper;

import org.mapstruct.*;
import uz.tuit.domain.District;
import uz.tuit.domain.Hospital;
import uz.tuit.domain.Region;
import uz.tuit.service.dto.DistrictDTO;
import uz.tuit.service.dto.HospitalDTO;
import uz.tuit.service.dto.RegionDTO;

/**
 * Mapper for the entity {@link Hospital} and its DTO {@link HospitalDTO}.
 */
@Mapper(componentModel = "spring")
public interface HospitalMapper extends EntityMapper<HospitalDTO, Hospital> {
    @Mapping(target = "region", source = "region", qualifiedByName = "regionId")
    @Mapping(target = "district", source = "district", qualifiedByName = "districtId")
    HospitalDTO toDto(Hospital s);

    @Named("regionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegionDTO toDtoRegionId(Region region);

    @Named("districtId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DistrictDTO toDtoDistrictId(District district);
}
