package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.entity.ForecastEntity;

import java.util.List;


public interface WeatherService {
    ForecastDto getForecast(String name);
    List<RequestCounterDto> getRequestCounters();

}
