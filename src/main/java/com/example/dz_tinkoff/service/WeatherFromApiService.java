package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.dto.WeatherApiResponseDto;

import java.time.LocalDateTime;

public interface WeatherFromApiService {
    WeatherApiResponseDto getWeatherFromApi(String cityName, double lat, double lon, LocalDateTime date);
}
