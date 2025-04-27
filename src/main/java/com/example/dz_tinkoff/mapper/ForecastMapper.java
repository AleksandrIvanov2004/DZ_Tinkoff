package com.example.dz_tinkoff.mapper;

import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.entity.ForecastEntity;
import org.springframework.stereotype.Component;

@Component
public class ForecastMapper {
    public ForecastDto mapToDto(ForecastEntity forecastEntity) {
        return new ForecastDto(forecastEntity.getId(), forecastEntity.getCityId(),
                forecastEntity.getTemperature(), forecastEntity.getDate());
    }

    public ForecastEntity mapToEntity(ForecastDto forecastDto) {
        return new ForecastEntity(forecastDto.getId(), forecastDto.getCityId(),
                forecastDto.getTemperature(), forecastDto.getDate());
    }
}
