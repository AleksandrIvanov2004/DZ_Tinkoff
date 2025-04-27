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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final ForecastRepository forecastRepository;
    private final ForecastMapper forecastMapper;
    private final RequestCounterRepository requestCounterRepository;
    private final RequestCounterMapper requestCounterMapper;

    public WeatherServiceImpl(CityRepository cityRepository, CityMapper cityMapper,
                              ForecastRepository forecastRepository, ForecastMapper forecastMapper,
                              RequestCounterRepository requestCounterRepository,
                              RequestCounterMapper requestCounterMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.forecastRepository = forecastRepository;
        this.forecastMapper = forecastMapper;
        this.requestCounterRepository = requestCounterRepository;
        this.requestCounterMapper = requestCounterMapper;
    }

    @Override
    public List<CityDto> getCities() {
        List<CityEntity> catEntities = cityRepository.getAll();
        return catEntities.stream()
                .map(cityMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto getCity(Long id) {
        CityEntity cityEntity = cityRepository.getById(id);
        return cityMapper.mapToDto(cityEntity);
    }

    @Override
    public void createCity(CityDto cityDto) {
        cityRepository.save(cityMapper.mapToEntity(cityDto));
    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }


    @Override
    public List<ForecastDto> getForecasts() {
        List<ForecastEntity> forecastEntities = forecastRepository.getAll();
        return forecastEntities.stream()
                .map(forecastMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ForecastDto getForecast(Long id) {
        ForecastEntity forecastEntity = forecastRepository.getById(id);
        return forecastMapper.mapToDto(forecastEntity);
    }

    @Override
    public void createForecast(ForecastDto forecastDto) {
        forecastRepository.save(forecastMapper.mapToEntity(forecastDto));
    }

    @Override
    public void deleteForecast(Long id) {
        forecastRepository.deleteById(id);
    }

    @Override
    public List<RequestCounterDto> getRequestCounters() {
        List<RequestCounterEntity> requestCounterEntities = requestCounterRepository.getAll();
        return requestCounterEntities.stream()
                .map(requestCounterMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestCounterDto getRequestCounter(Long city_id) {
        RequestCounterEntity requestCounterEntity = requestCounterRepository.getByCityId(city_id);
        return requestCounterMapper.mapToDto(requestCounterEntity);
    }
}
