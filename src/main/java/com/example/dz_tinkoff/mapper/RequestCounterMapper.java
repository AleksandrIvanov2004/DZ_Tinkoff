package com.example.dz_tinkoff.mapper;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestCounterMapper {
     RequestCounterDto mapToDto(RequestCounterEntity requestCounterEntity);
     RequestCounterEntity mapToEntity(RequestCounterDto requestCounterDto);
}
