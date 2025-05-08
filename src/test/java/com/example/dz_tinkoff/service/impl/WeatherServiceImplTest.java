package com.example.dz_tinkoff.service.impl;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.repository.CityRepository;
import com.example.dz_tinkoff.repository.ForecastRepository;
import com.example.dz_tinkoff.repository.RequestCounterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {
    @Mock
    private CityRepository cityRepository;

    @Mock
    private ForecastRepository forecastRepository;

    @Mock
    private RequestCounterRepository requestCounterRepository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    @Transactional
    void getForecast_ShouldCallAllRepositories() {
        String cityName = "Москва";
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(cityName);

        when(cityRepository.getCityByName(cityName)).thenReturn(cityEntity);

        weatherService.getForecast(cityName);

        verify(cityRepository).addCity(cityName);
        verify(cityRepository).getCityByName(cityName);
        verify(forecastRepository).getForecast(cityEntity);
        verify(requestCounterRepository).updateRequestsByCityId(cityEntity);
    }


    @Test
    @Transactional
    void getForecast_ShouldHandleExistingCity() {
        // Arrange
        String cityName = "Санкт-Петербург";
        CityEntity existingCity = new CityEntity();
        existingCity.setName(cityName);

        when(cityRepository.getCityByName(cityName)).thenReturn(existingCity);

        weatherService.getForecast(cityName);

        verify(cityRepository).addCity(cityName);
        verify(forecastRepository).getForecast(existingCity);
    }

    @Test
    @Transactional
    void getForecast_ShouldUpdateRequestCounter() {
        String cityName = "Новосибирск";
        CityEntity city = new CityEntity();
        city.setId(1L);
        city.setName(cityName);

        when(cityRepository.getCityByName(cityName)).thenReturn(city);

        weatherService.getForecast(cityName);

        verify(requestCounterRepository).updateRequestsByCityId(city);
    }
}
