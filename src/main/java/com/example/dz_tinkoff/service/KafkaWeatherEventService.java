package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.dto.WeatherRequestMetadataDto;

public interface KafkaWeatherEventService {
    void publishWeatherRequest(String cityName, WeatherRequestMetadataDto metadata);
}
