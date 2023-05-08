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
    @Mapping(target = "region", source = "region", qualifiedByName = "regionName")
    @Mapping(target = "district", source = "district", qualifiedByName = "districtName")
    HospitalDTO toDto(Hospital s);

    @Named("regionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionDTO toDtoRegionName(Region region);

    @Named("districtName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DistrictDTO toDtoDistrictName(District district);
}
