package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.*;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import com.example.dz_tinkoff.mapper.ForecastMapper;
import com.example.dz_tinkoff.mapper.RequestCounterMapper;
import com.example.dz_tinkoff.repository.CityRepository;
import com.example.dz_tinkoff.repository.ForecastRepository;
import com.example.dz_tinkoff.repository.RequestCounterRepository;
import com.example.dz_tinkoff.service.CoordinatesFromApiService;
import com.example.dz_tinkoff.service.KafkaWeatherEventService;
import com.example.dz_tinkoff.service.WeatherFromApiService;
import com.example.dz_tinkoff.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final CityRepository cityRepository;
    private final ForecastRepository forecastRepository;
    private final RequestCounterRepository requestCounterRepository;
    private final RequestCounterMapper requestCounterMapper;
    private final ForecastMapper forecastMapper;
    private final CoordinatesFromApiService coordinatesFromApiService;
    private final KafkaWeatherEventService kafkaWeatherEventService;
    private final WeatherFromApiService weatherFromApiService;


    @Override
    @Transactional
    public ForecastDto getForecast(String cityName, LocalDateTime date) {

        CityCoordinatesDto coords = coordinatesFromApiService.getCityCoordinates(cityName);
        cityRepository.addCity(
                cityName,
                coords.getLatitude(),
                coords.getLongitude()
        );

        CityEntity city = cityRepository.getCityByName(cityName);

        WeatherApiResponseDto weatherData = weatherFromApiService.getWeatherFromApi(cityName,
                city.getCoordX(), city.getCoordY(), date);

        ForecastEntity forecast = new ForecastEntity();
        forecast.setCity(city);
        forecast.setTemperature(weatherData.getTemp2Cel());
        forecast.setWindSpeed(weatherData.getWindSpeed10());
        forecast.setDate(Timestamp.valueOf(date));

        WeatherRequestMetadataDto metadata = new WeatherRequestMetadataDto(
                cityName,
                Instant.now()
        );
        kafkaWeatherEventService.publishWeatherRequest(cityName, metadata);

        return forecastMapper.mapToDto(forecastRepository.save(forecast));
    }


    @Override
    public List<RequestCounterDto> getRequestCounters() {
        return requestCounterRepository.findAll().stream()
                .map(requestCounterMapper::mapToDto)
                .collect(Collectors.toList());
    }
}