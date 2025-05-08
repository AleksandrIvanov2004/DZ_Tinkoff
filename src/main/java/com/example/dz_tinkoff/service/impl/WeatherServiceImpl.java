package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import com.example.dz_tinkoff.mapper.CityMapper;
import com.example.dz_tinkoff.mapper.ForecastMapper;
import com.example.dz_tinkoff.mapper.RequestCounterMapper;
import com.example.dz_tinkoff.repository.CityRepository;
import com.example.dz_tinkoff.repository.ForecastRepository;
import com.example.dz_tinkoff.repository.RequestCounterRepository;
import com.example.dz_tinkoff.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final ForecastRepository forecastRepository;
    private final ForecastMapper forecastMapper;
    private final RequestCounterRepository requestCounterRepository;
    private final RequestCounterMapper requestCounterMapper;


    @Override
    @Transactional
    public void getForecast(String cityName) {
        cityRepository.addCity(cityName);
        CityEntity cityEntity = cityRepository.getCityByName(cityName);
        forecastRepository.getForecast(cityEntity);
        requestCounterRepository.updateRequestsByCityId(cityEntity);
    }

    @Override
    public List<RequestCounterDto> getRequestCounters() {
        List<RequestCounterEntity> requestCounterEntities = requestCounterRepository.findAll();
        return requestCounterEntities.stream()
                .map(requestCounterMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
