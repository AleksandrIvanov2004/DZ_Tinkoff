package com.example.dz_tinkoff.mapper;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import org.springframework.stereotype.Component;

@Component
public class RequestCounterMapper {
    public RequestCounterDto mapToDto(RequestCounterEntity requestCounterEntity) {
        return new RequestCounterDto(requestCounterEntity.getCityId(), requestCounterEntity.getRequestCount(),
                requestCounterEntity.getLastAccessDatetime());
    }

    public RequestCounterEntity mapToEntity(RequestCounterDto requestCounterDto) {
        return new RequestCounterEntity(requestCounterDto.getCityId(), requestCounterDto.getRequestCount(),
                requestCounterDto.getLastAccessDatetime());
    }
}
