package com.example.dz_tinkoff.mapper;

import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.entity.ForecastEntity;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ForecastMapper {
    ForecastDto mapToDto(ForecastEntity forecastEntity);
    ForecastEntity mapToEntity(ForecastDto forecastDto);
}
