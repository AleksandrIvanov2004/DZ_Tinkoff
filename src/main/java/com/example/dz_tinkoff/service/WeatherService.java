package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;

import java.util.List;

public interface WeatherService { // это простой CRUD
    List<CityDto> getCities();

    CityDto getCity(Long id);

    void createCity(CityDto catDto);

    void deleteCity(Long id);


    List<ForecastDto> getForecasts();

    ForecastDto getForecast(Long id);

    void createForecast(ForecastDto forecastDto);

    void deleteForecast(Long id);


    List<RequestCounterDto> getRequestCounters();
    RequestCounterDto getRequestCounter(Long city_id);
}
