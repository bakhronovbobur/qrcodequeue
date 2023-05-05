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
    @Mapping(target = "region", source = "region", qualifiedByName = "regionId")
    DistrictDTO toDto(District s);

    @Named("regionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegionDTO toDtoRegionId(Region region);
}
