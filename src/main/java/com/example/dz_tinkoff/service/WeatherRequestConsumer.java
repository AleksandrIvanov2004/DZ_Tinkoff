package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.controller.ForecastController;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import com.example.dz_tinkoff.repository.CityRepository;
import com.example.dz_tinkoff.repository.RequestCounterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherRequestConsumer {

    private final RequestCounterRepository requestCounterRepository;
    private final CityRepository cityRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private String popularCityTopic = "popular-city-stats";

    private String peakHourTopic = "peak-hour-stats";

    @KafkaListener(topics = "weather-requests")
    public void consumeWeatherRequest(String message) {
        try {
            ForecastController.WeatherRequestMetadata metadata = objectMapper.readValue(
                    message,
                    ForecastController.WeatherRequestMetadata.class
            );

            CityEntity city = cityRepository.getCityByName(metadata.getCity());

            RequestCounterEntity counter = requestCounterRepository.findById(city.getId())
                    .orElse(new RequestCounterEntity(city.getId(), city, 0, null));

            counter.setRequestCount(counter.getRequestCount() + 1);
            counter.setLastAccessDatetime(Timestamp.from(metadata.getRequestTime()));
            requestCounterRepository.save(counter);


            calculateAndSendStats();

        } catch (Exception e) {
            log.error("Error processing weather request", e);
        }
    }

    Timestamp monthAgo = Timestamp.from(Instant.now().minus(30, ChronoUnit.DAYS));

    private void calculateAndSendStats() {
        String popularCity = requestCounterRepository.findMostPopularCityLastMonth(monthAgo)
                .orElse("No data");

        Integer peakHour = requestCounterRepository.findPeakHourLastMonth(monthAgo)
                .orElse(null);

        try {
            kafkaTemplate.send(
                    popularCityTopic,
                    popularCity,
                    objectMapper.writeValueAsString(new PopularCityStats(popularCity))
            );

            if (peakHour != null) {
                kafkaTemplate.send(
                        peakHourTopic,
                        String.valueOf(peakHour),
                        objectMapper.writeValueAsString(new PeakHourStats(peakHour))
                );
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize stats", e);
        }
    }

    @Data
    @AllArgsConstructor
    private static class PopularCityStats {
        private String city;
    }

    @Data
    @AllArgsConstructor
    private static class PeakHourStats {
        private int hour;
    }
}
