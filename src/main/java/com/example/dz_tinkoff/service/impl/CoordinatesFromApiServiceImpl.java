package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.CityCoordinatesDto;
import com.example.dz_tinkoff.service.CoordinatesFromApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CoordinatesFromApiServiceImpl implements CoordinatesFromApiService {
    private static final String YANDEX_GEOCODE_API = "https://geocode-maps.yandex.ru/v1";
    private static final String API_KEY = "be1242a5-4916-4f50-b7ba-17753df6f8cd";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CoordinatesFromApiServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    @Cacheable(value = "coordinates", key = "#cityName", cacheManager = "coordinatesCacheManager")
    public CityCoordinatesDto getCityCoordinates(String cityName) {
        String encodedCityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8);

        String url = UriComponentsBuilder.fromHttpUrl(YANDEX_GEOCODE_API)
                .queryParam("apikey", API_KEY)
                .queryParam("geocode", cityName)
                .queryParam("format", "json")
                .build()
                .toUriString();

        log.debug("Сформированный URL запроса: {}", url);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode posNode = rootNode.path("response")
                    .path("GeoObjectCollection")
                    .path("featureMember")
                    .get(0)
                    .path("GeoObject")
                    .path("Point")
                    .path("pos");

            if (posNode.isMissingNode()) {
                log.error("Координаты не найдены в ответе API для города: {}", cityName);
                throw new RuntimeException("Координаты не найдены в ответе от API");
            }

            String[] coordinates = posNode.asText().split(" ");
            double coordX = Double.parseDouble(coordinates[0]);
            double coordY = Double.parseDouble(coordinates[1]);

            log.info("Найдены координаты для города {}: {}, {}", cityName, coordX, coordY);

            return new CityCoordinatesDto(coordX, coordY);
        } catch (Exception e) {
            log.error("Ошибка при получении координат для города {}: {}", cityName, e.getMessage());
            throw new RuntimeException("Ошибка при получении координат для города: " + cityName, e);
        }
    }
}