package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import com.example.dz_tinkoff.mapper.ForecastMapper;
import com.example.dz_tinkoff.mapper.RequestCounterMapper;
import com.example.dz_tinkoff.repository.CityRepository;
import com.example.dz_tinkoff.repository.ForecastRepository;
import com.example.dz_tinkoff.repository.RequestCounterRepository;
import com.example.dz_tinkoff.service.TemperatureService;
import com.example.dz_tinkoff.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private final TemperatureService temperatureService;


    @Override
    @Transactional
    public ForecastDto getForecast(String cityName) {
        // Всегда получаем актуальные данные города
        cityRepository.addCity(cityName);
        CityEntity cityEntity = cityRepository.getCityByName(cityName);

        // Берем температуру из кеша
        int temperature = temperatureService.getCachedTemperature(cityName);

        // Создаем новый прогноз с текущей датой
        ForecastEntity forecast = new ForecastEntity();
        forecast.setCity(cityEntity);
        forecast.setTemperature(temperature);
        forecast.setDate(Timestamp.valueOf(LocalDateTime.now()));

        return forecastMapper.mapToDto(forecastRepository.save(forecast));
    }


    @Override
    public List<RequestCounterDto> getRequestCounters() {
        return requestCounterRepository.findAll().stream()
                .map(requestCounterMapper::mapToDto)
                .collect(Collectors.toList());
    }
}