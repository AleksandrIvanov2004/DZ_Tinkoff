package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.CityCoordinatesDto;
import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.WeatherApiResponseDto;
import com.example.dz_tinkoff.dto.WeatherRequestMetadataDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import com.example.dz_tinkoff.mapper.ForecastMapper;
import com.example.dz_tinkoff.mapper.RequestCounterMapper;
import com.example.dz_tinkoff.repository.CityRepository;
import com.example.dz_tinkoff.repository.ForecastRepository;
import com.example.dz_tinkoff.repository.RequestCounterRepository;
import com.example.dz_tinkoff.service.CoordinatesFromApiService;
import com.example.dz_tinkoff.service.KafkaWeatherEventService;
import com.example.dz_tinkoff.service.WeatherFromApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    // Все зависимости сервиса
    @Mock private CityRepository cityRepository;
    @Mock private ForecastRepository forecastRepository;
    @Mock private RequestCounterRepository requestCounterRepository;
    @Mock private RequestCounterMapper requestCounterMapper;
    @Mock private ForecastMapper forecastMapper;
    @Mock private CoordinatesFromApiService coordinatesFromApiService;
    @Mock private KafkaWeatherEventService kafkaWeatherEventService;
    @Mock private WeatherFromApiService weatherFromApiService;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private final String TEST_CITY = "Москва";
    private final LocalDateTime TEST_DATE = LocalDateTime.now();
    private final double TEST_LAT = 55.75;
    private final double TEST_LON = 37.61;
    private final double TEST_TEMP = 20.5;
    private final double TEST_WIND = 5.0;

    @Test
    void getForecast_ShouldCallAddCityWithCoordinates() {
        CityCoordinatesDto coords = new CityCoordinatesDto(TEST_LAT, TEST_LON);
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(TEST_CITY);
        cityEntity.setCoordX(TEST_LAT);
        cityEntity.setCoordY(TEST_LON);

        WeatherApiResponseDto weatherResponse = new WeatherApiResponseDto();
        weatherResponse.setTemp2Cel(TEST_TEMP);
        weatherResponse.setWindSpeed10(TEST_WIND);

        ForecastEntity forecastEntity = new ForecastEntity();
        forecastEntity.setCity(cityEntity);
        forecastEntity.setTemperature(TEST_TEMP);
        forecastEntity.setWindSpeed(TEST_WIND);
        forecastEntity.setDate(Timestamp.valueOf(TEST_DATE));

        ForecastDto forecastDto = new ForecastDto(1L, cityEntity.getId(), TEST_TEMP, Timestamp.valueOf(TEST_DATE), TEST_WIND);

        when(coordinatesFromApiService.getCityCoordinates(TEST_CITY)).thenReturn(coords);
        when(cityRepository.getCityByName(TEST_CITY)).thenReturn(cityEntity);
        when(weatherFromApiService.getWeatherFromApi(TEST_CITY, TEST_LAT, TEST_LON, TEST_DATE))
                .thenReturn(weatherResponse);
        when(forecastRepository.save(any(ForecastEntity.class))).thenReturn(forecastEntity);
        when(forecastMapper.mapToDto(forecastEntity)).thenReturn(forecastDto);

        weatherService.getForecast(TEST_CITY, TEST_DATE);

        verify(cityRepository).addCity(TEST_CITY, TEST_LAT, TEST_LON);
    }

    @Test
    void getForecast_ShouldCallGetCityByNameWithCoordinates() {
        CityCoordinatesDto coords = new CityCoordinatesDto(TEST_LAT, TEST_LON);
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(TEST_CITY);
        cityEntity.setCoordX(TEST_LAT);
        cityEntity.setCoordY(TEST_LON);

        WeatherApiResponseDto weatherResponse = new WeatherApiResponseDto();
        weatherResponse.setTemp2Cel(TEST_TEMP);
        weatherResponse.setWindSpeed10(TEST_WIND);

        ForecastEntity forecastEntity = new ForecastEntity();
        forecastEntity.setCity(cityEntity);
        forecastEntity.setTemperature(TEST_TEMP);
        forecastEntity.setWindSpeed(TEST_WIND);
        forecastEntity.setDate(Timestamp.valueOf(TEST_DATE));

        ForecastDto forecastDto = new ForecastDto(1L, cityEntity.getId(), TEST_TEMP, Timestamp.valueOf(TEST_DATE), TEST_WIND);

        when(coordinatesFromApiService.getCityCoordinates(TEST_CITY)).thenReturn(coords);
        when(cityRepository.getCityByName(TEST_CITY)).thenReturn(cityEntity);
        when(weatherFromApiService.getWeatherFromApi(TEST_CITY, TEST_LAT, TEST_LON, TEST_DATE))
                .thenReturn(weatherResponse);
        when(forecastRepository.save(any(ForecastEntity.class))).thenReturn(forecastEntity);
        when(forecastMapper.mapToDto(forecastEntity)).thenReturn(forecastDto);

        weatherService.getForecast(TEST_CITY, TEST_DATE);

        verify(cityRepository).getCityByName(TEST_CITY);
    }

    @Test
    void getForecast_ShouldCallGetCityCoordinatesWithCoordinates() {
        CityCoordinatesDto coords = new CityCoordinatesDto(TEST_LAT, TEST_LON);
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(TEST_CITY);
        cityEntity.setCoordX(TEST_LAT);
        cityEntity.setCoordY(TEST_LON);

        WeatherApiResponseDto weatherResponse = new WeatherApiResponseDto();
        weatherResponse.setTemp2Cel(TEST_TEMP);
        weatherResponse.setWindSpeed10(TEST_WIND);

        ForecastEntity forecastEntity = new ForecastEntity();
        forecastEntity.setCity(cityEntity);
        forecastEntity.setTemperature(TEST_TEMP);
        forecastEntity.setWindSpeed(TEST_WIND);
        forecastEntity.setDate(Timestamp.valueOf(TEST_DATE));

        ForecastDto forecastDto = new ForecastDto(1L, cityEntity.getId(), TEST_TEMP, Timestamp.valueOf(TEST_DATE), TEST_WIND);

        when(coordinatesFromApiService.getCityCoordinates(TEST_CITY)).thenReturn(coords);
        when(cityRepository.getCityByName(TEST_CITY)).thenReturn(cityEntity);
        when(weatherFromApiService.getWeatherFromApi(TEST_CITY, TEST_LAT, TEST_LON, TEST_DATE))
                .thenReturn(weatherResponse);
        when(forecastRepository.save(any(ForecastEntity.class))).thenReturn(forecastEntity);
        when(forecastMapper.mapToDto(forecastEntity)).thenReturn(forecastDto);

        weatherService.getForecast(TEST_CITY, TEST_DATE);

        verify(coordinatesFromApiService).getCityCoordinates(TEST_CITY);
    }

    @Test
    void getForecast_ShouldCallGetWeatherFromApiWithCoordinates() {
        CityCoordinatesDto coords = new CityCoordinatesDto(TEST_LAT, TEST_LON);
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(TEST_CITY);
        cityEntity.setCoordX(TEST_LAT);
        cityEntity.setCoordY(TEST_LON);

        WeatherApiResponseDto weatherResponse = new WeatherApiResponseDto();
        weatherResponse.setTemp2Cel(TEST_TEMP);
        weatherResponse.setWindSpeed10(TEST_WIND);

        ForecastEntity forecastEntity = new ForecastEntity();
        forecastEntity.setCity(cityEntity);
        forecastEntity.setTemperature(TEST_TEMP);
        forecastEntity.setWindSpeed(TEST_WIND);
        forecastEntity.setDate(Timestamp.valueOf(TEST_DATE));

        ForecastDto forecastDto = new ForecastDto(1L, cityEntity.getId(), TEST_TEMP, Timestamp.valueOf(TEST_DATE), TEST_WIND);

        when(coordinatesFromApiService.getCityCoordinates(TEST_CITY)).thenReturn(coords);
        when(cityRepository.getCityByName(TEST_CITY)).thenReturn(cityEntity);
        when(weatherFromApiService.getWeatherFromApi(TEST_CITY, TEST_LAT, TEST_LON, TEST_DATE))
                .thenReturn(weatherResponse);
        when(forecastRepository.save(any(ForecastEntity.class))).thenReturn(forecastEntity);
        when(forecastMapper.mapToDto(forecastEntity)).thenReturn(forecastDto);

        weatherService.getForecast(TEST_CITY, TEST_DATE);

        verify(weatherFromApiService).getWeatherFromApi(TEST_CITY, TEST_LAT, TEST_LON, TEST_DATE);
    }

    @Test
    void getForecast_ShouldCallSaveWithCoordinates() {
        CityCoordinatesDto coords = new CityCoordinatesDto(TEST_LAT, TEST_LON);
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(TEST_CITY);
        cityEntity.setCoordX(TEST_LAT);
        cityEntity.setCoordY(TEST_LON);

        WeatherApiResponseDto weatherResponse = new WeatherApiResponseDto();
        weatherResponse.setTemp2Cel(TEST_TEMP);
        weatherResponse.setWindSpeed10(TEST_WIND);

        ForecastEntity forecastEntity = new ForecastEntity();
        forecastEntity.setCity(cityEntity);
        forecastEntity.setTemperature(TEST_TEMP);
        forecastEntity.setWindSpeed(TEST_WIND);
        forecastEntity.setDate(Timestamp.valueOf(TEST_DATE));

        ForecastDto forecastDto = new ForecastDto(1L, cityEntity.getId(), TEST_TEMP, Timestamp.valueOf(TEST_DATE), TEST_WIND);

        when(coordinatesFromApiService.getCityCoordinates(TEST_CITY)).thenReturn(coords);
        when(cityRepository.getCityByName(TEST_CITY)).thenReturn(cityEntity);
        when(weatherFromApiService.getWeatherFromApi(TEST_CITY, TEST_LAT, TEST_LON, TEST_DATE))
                .thenReturn(weatherResponse);
        when(forecastRepository.save(any(ForecastEntity.class))).thenReturn(forecastEntity);
        when(forecastMapper.mapToDto(forecastEntity)).thenReturn(forecastDto);

        weatherService.getForecast(TEST_CITY, TEST_DATE);

        verify(forecastRepository).save(any(ForecastEntity.class));
    }

    @Test
    void getForecast_ShouldHandleApiError_WhenCoordinatesServiceFails() {
        when(coordinatesFromApiService.getCityCoordinates(TEST_CITY))
                .thenThrow(new RuntimeException("Coordinates service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            weatherService.getForecast(TEST_CITY, TEST_DATE);
        });

        verifyNoInteractions(weatherFromApiService, forecastRepository, kafkaWeatherEventService);
    }

    @Test
    void getForecast_ShouldHandleApiError_WhenWeatherServiceFails() {
        CityCoordinatesDto coords = new CityCoordinatesDto(TEST_LAT, TEST_LON);
        CityEntity cityEntity = new CityEntity(1L, TEST_CITY, TEST_LAT, TEST_LON);

        when(coordinatesFromApiService.getCityCoordinates(TEST_CITY)).thenReturn(coords);
        when(cityRepository.getCityByName(TEST_CITY)).thenReturn(cityEntity);
        when(weatherFromApiService.getWeatherFromApi(TEST_CITY, TEST_LAT, TEST_LON, TEST_DATE))
                .thenThrow(new RuntimeException("Сервис погоды недоступен"));

        assertThrows(RuntimeException.class, () -> {
            weatherService.getForecast(TEST_CITY, TEST_DATE);
        });

        verifyNoInteractions(forecastRepository, kafkaWeatherEventService);
    }
}