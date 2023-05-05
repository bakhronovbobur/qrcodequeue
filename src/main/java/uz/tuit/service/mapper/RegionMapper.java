package uz.tuit.service.mapper;

import org.mapstruct.*;
import uz.tuit.domain.Region;
import uz.tuit.service.dto.RegionDTO;

/**
 * Mapper for the entity {@link Region} and its DTO {@link RegionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegionMapper extends EntityMapper<RegionDTO, Region> {}
