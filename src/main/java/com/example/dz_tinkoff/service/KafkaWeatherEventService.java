package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.dto.WeatherRequestMetadata;

public interface KafkaWeatherEventService {
    public void publishWeatherRequest(String cityName, WeatherRequestMetadata metadata);
}
