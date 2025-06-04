package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.WeatherApiResponseDto;
import com.example.dz_tinkoff.service.WeatherFromApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WeatherFromApiServiceImpl implements WeatherFromApiService {
    private final RestTemplate restTemplate;
    private static final Set<String> CACHED_CITIES = Set.of(
            "Москва", "Санкт-Петербург", "Энгельс", "Саратов"
    );

    public Set<String> getCachedCities() {
        return CACHED_CITIES;
    }

    @Override
    @Cacheable(value = "weatherForecast", key = "{#cityName, #date}",
            cacheManager = "forecastCacheManager", unless = "!#root.target.getCachedCities().contains(#cityName)")
    public WeatherApiResponseDto getWeatherFromApi(String cityName, double lat, double lon, LocalDateTime date) {
        String url = UriComponentsBuilder.fromHttpUrl("https://projecteol.ru/api/weather/")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("date", date.toString())
                .queryParam("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzQ4Nzc4NTk2LCJpYXQiOjE3NDg3NzgyOTYsImp0aSI6IjcxM2IzZTNkMjg1OTQxODc4MzdhMjA3MGQ4YTMzNjk1IiwidXNlcl9pZCI6MTA3Nn0.RoYDvRQ42dApj4yjanTqMEO-qYIBlszkNkVSjWiPNDE")
                .toUriString();

        ResponseEntity<WeatherApiResponseDto[]> response = restTemplate.getForEntity(
                url,
                WeatherApiResponseDto[].class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
            WeatherApiResponseDto result = response.getBody()[0];
            return result;
        }

        throw new RuntimeException("Ошибка получения погоды из API");
    }
}
