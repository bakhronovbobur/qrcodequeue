package uz.tuit.service.mapper;

import org.mapstruct.*;
import uz.tuit.domain.District;
import uz.tuit.domain.Region;
import uz.tuit.service.dto.DistrictDTO;
import uz.tuit.service.dto.RegionDTO;

/**
 * Mapper for the entity {@link District} and its DTO {@link DistrictDTO}.
 */
@Mapper(componentModel = "spring")
public interface DistrictMapper extends EntityMapper<DistrictDTO, District> {
    @Mapping(target = "region", source = "region", qualifiedByName = "regionName")
    DistrictDTO toDto(District s);

    @Named("regionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionDTO toDtoRegionName(Region region);
}
