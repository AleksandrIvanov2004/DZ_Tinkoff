package com.example.dz_tinkoff.service.impl;
import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import com.example.dz_tinkoff.mapper.ForecastMapper;
import com.example.dz_tinkoff.repository.CityRepository;
import com.example.dz_tinkoff.repository.ForecastRepository;
import com.example.dz_tinkoff.repository.RequestCounterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {
    private static final int MIN_TEMP = -50;
    private static final int MAX_TEMP = 50;


    @Mock
    private CityRepository cityRepository;

    @Mock
    private ForecastRepository forecastRepository;

    @Mock
    private RequestCounterRepository requestCounterRepository;

    @Mock
    private ForecastMapper forecastMapper;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    void getForecast_ShouldCallAllAddCity() {
        String cityName = "Москва";
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(cityName);

        when(cityRepository.getCityByName(cityName)).thenReturn(cityEntity);
        when(forecastRepository.getForecast(any(), anyInt()))
                .thenReturn(new ForecastEntity());

        weatherService.getForecast(cityName);

        verify(cityRepository).addCity(cityName);
    }

    @Test
    void getForecast_ShouldCallGetCityByNameRepository() {
        String cityName = "Москва";
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(cityName);

        when(cityRepository.getCityByName(cityName)).thenReturn(cityEntity);
        when(forecastRepository.getForecast(any(), anyInt()))
                .thenReturn(new ForecastEntity());

        weatherService.getForecast(cityName);

        verify(cityRepository).getCityByName(cityName);
    }


    @Test
    void getForecast_ShouldCallGetForecastRepository() {
        String cityName = "Москва";
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(cityName);

        when(cityRepository.getCityByName(cityName)).thenReturn(cityEntity);
        when(forecastRepository.getForecast(any(), anyInt()))
                .thenReturn(new ForecastEntity());

        weatherService.getForecast(cityName);

        verify(forecastRepository).getForecast(eq(cityEntity), anyInt());
    }

    @Test
    void getForecast_ShouldCallGetForecastMapper() {
        String cityName = "Москва";
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(cityName);

        when(cityRepository.getCityByName(cityName)).thenReturn(cityEntity);
        when(forecastRepository.getForecast(any(), anyInt()))
                .thenReturn(new ForecastEntity());

        weatherService.getForecast(cityName);

        verify(forecastMapper).mapToDto(any());
    }

    @Test
    void getForecast_ShouldGenerateTemperatureInValidRange() {
        String cityName = "Москва";
        int testTemperature = 15;

        when(forecastMapper.mapToDto(any())).
                thenReturn(new ForecastDto(null, null, testTemperature, null));

        ForecastDto result =  weatherService.getForecast(cityName);

        assertTrue(result.temperature() >= -50 && result.temperature() <= 50,
                "Температура " + result.temperature() + " вне диапазона [-50, 50]");
    }


    @Test
    void getForecast_ShouldReturnForecastEntityType() {
        String cityName = "Москва";

        when(forecastMapper.mapToDto(any())).
                thenReturn(new ForecastDto(null, null, 0, null));

        ForecastDto result = weatherService.getForecast(cityName);

        assertNotNull(result, "Метод должен возвращать не-null объект");
        assertTrue(result instanceof ForecastDto,
                "Возвращаемый объект должен быть типа ForecastEntity");
    }
}
