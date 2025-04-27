package com.example.dz_tinkoff.mapper;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.entity.CityEntity;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public CityDto mapToDto(CityEntity cityEntity) {
        return new CityDto(cityEntity.getId(), cityEntity.getName());
    }

    public CityEntity mapToEntity(CityDto cityDto) {
        return new CityEntity(cityDto.getId(), cityDto.getName());
    }
}
