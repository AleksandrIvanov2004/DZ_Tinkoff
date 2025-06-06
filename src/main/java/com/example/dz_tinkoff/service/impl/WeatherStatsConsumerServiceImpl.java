package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.PeakHourStatsDto;
import com.example.dz_tinkoff.dto.PopularCityStatsDto;
import com.example.dz_tinkoff.service.WeatherStatsConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherStatsConsumerServiceImpl implements WeatherStatsConsumerService {

    private final ObjectMapper objectMapper;

    // Кэш для хранения последних данных (можно заменить на Redis)
    private final Map<String, Object> statsCache = new HashMap<>();

    @Override
    @KafkaListener(topics = "popular-city-stats", groupId = "weather-stats-group")
    public PopularCityStatsDto consumePopularCityStats(String message) {
        try {
            PopularCityStatsDto stats = objectMapper.readValue(message, PopularCityStatsDto.class);
            log.info("Получена статистика по популярному городу: {}", stats.getCity());

            // Сохраняем в кэш
            statsCache.put("popularCity", stats);

            return stats;
        } catch (JsonProcessingException e) {
            log.error("Ошибка при десериализации popular-city-stats", e);
            throw new RuntimeException("Invalid message format", e);
        }
    }

    @Override
    @KafkaListener(topics = "peak-hour-stats", groupId = "weather-stats-group")
    public PeakHourStatsDto consumePeakHourStats(String message) {
        try {
            PeakHourStatsDto stats = objectMapper.readValue(message, PeakHourStatsDto.class);
            log.info("Получена статистика по пиковому часу: {}", stats.getHour());

            // Сохраняем в кэш
            statsCache.put("peakHour", stats);

            return stats;
        } catch (JsonProcessingException e) {
            log.error("Ошибка при десериализации peak-hour-stats", e);
            throw new RuntimeException("Invalid message format", e);
        }
    }

    @Override
    public Optional<PopularCityStatsDto> getLastPopularCityStats() {
        return Optional.ofNullable((PopularCityStatsDto) statsCache.get("popularCity"));
    }

    @Override
    public Optional<PeakHourStatsDto> getLastPeakHourStats() {
        return Optional.ofNullable((PeakHourStatsDto) statsCache.get("peakHour"));
    }
}