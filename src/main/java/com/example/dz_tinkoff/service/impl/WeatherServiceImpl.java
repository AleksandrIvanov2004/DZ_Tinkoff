package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
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
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final CityRepository cityRepository;
    private final ForecastRepository forecastRepository;
    private final RequestCounterRepository requestCounterRepository;
    private final RequestCounterMapper requestCounterMapper;
    private final ForecastMapper forecastMapper;
    int MIN_TEMP = -50;
    int MAX_TEMP = 50;
    Random random = new Random();


    @Override
    @Transactional
    public ForecastDto getForecast(String cityName) {
        cityRepository.addCity(cityName);
        CityEntity cityEntity = cityRepository.getCityByName(cityName);
        requestCounterRepository.updateRequestsByCityId(cityEntity);
        return forecastMapper.mapToDto(forecastRepository.getForecast
                (cityEntity, random.nextInt(MAX_TEMP - MIN_TEMP + 1) + MIN_TEMP));
    }

    @Override
    public List<RequestCounterDto> getRequestCounters() {
        List<RequestCounterEntity> requestCounterEntities = requestCounterRepository.findAll();
        return requestCounterEntities.stream()
                .map(requestCounterMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
