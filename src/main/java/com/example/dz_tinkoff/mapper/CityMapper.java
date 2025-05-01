package com.example.dz_tinkoff.mapper;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.entity.CityEntity;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper {
    CityDto mapToDto(CityEntity cityEntity);
    CityEntity mapToEntity(CityDto cityDto);

}
