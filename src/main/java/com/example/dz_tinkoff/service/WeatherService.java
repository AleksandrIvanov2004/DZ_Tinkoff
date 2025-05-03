package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;

import java.util.List;


public interface WeatherService {
    void getForecast(String name);
    List<RequestCounterDto> getRequestCounters();

}
