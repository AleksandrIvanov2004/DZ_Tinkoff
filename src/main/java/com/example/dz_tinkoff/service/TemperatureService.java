package com.example.dz_tinkoff.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TemperatureService {
    private static final int MIN_TEMP = -50;
    private static final int MAX_TEMP = 50;
    private final Random random = new Random();

    @Cacheable(value = "forecastTemperature", key = "#cityName")
    public int getCachedTemperature(String cityName) {
        return random.nextInt(MAX_TEMP - MIN_TEMP + 1) + MIN_TEMP;
    }
}

