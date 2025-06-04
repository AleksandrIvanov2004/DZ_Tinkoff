package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.PeakHourStatsDto;
import com.example.dz_tinkoff.dto.PopularCityStatsDto;
import com.example.dz_tinkoff.dto.WeatherRequestMetadataDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import com.example.dz_tinkoff.repository.CityRepository;
import com.example.dz_tinkoff.repository.RequestCounterRepository;
import com.example.dz_tinkoff.service.WeatherRequestConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherRequestConsumerServiceImpl implements WeatherRequestConsumerService {
    private final RequestCounterRepository requestCounterRepository;
    private final CityRepository cityRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private String popularCityTopic = "popular-city-stats";
    private String peakHourTopic = "peak-hour-stats";

    @Override
    @KafkaListener(topics = "weather-requests")
    public void consumeWeatherRequest(String message) {
        log.info("Получено сообщение: {}", message);
        try {
            WeatherRequestMetadataDto metadata = objectMapper.readValue(
                    message,
                    WeatherRequestMetadataDto.class
            );

            CityEntity city = cityRepository.getCityByName(metadata.getCity());
            log.info("Id города: {}", city.getId());
            RequestCounterEntity counter = requestCounterRepository.findById(city.getId())
                    .orElse(new RequestCounterEntity(city.getId(), city, 0, null));

            counter.setRequestCount(counter.getRequestCount() + 1);
            log.info("Увеличили на 1");
            counter.setLastAccessDatetime(Timestamp.from(metadata.getRequestTime()));
            log.info("Добавили дату");
            requestCounterRepository.save(counter);
            log.info("Сохранили");

            calculateAndSendStats();

        } catch (Exception e) {
            log.error("Ошибка при обработке запроса погоды", e);
        }
    }

    private void calculateAndSendStats() {
        Timestamp monthAgo = Timestamp.from(Instant.now().minus(30, ChronoUnit.DAYS));
        String popularCity = requestCounterRepository.findMostPopularCityLastMonth(monthAgo)
                .orElse("No data");

        Integer peakHour = requestCounterRepository.findPeakHourLastMonth(monthAgo)
                .orElse(null);

        try {
            kafkaTemplate.send(
                    popularCityTopic,
                    popularCity,
                    objectMapper.writeValueAsString(new PopularCityStatsDto(popularCity))
            );

            if (peakHour != null) {
                kafkaTemplate.send(
                        peakHourTopic,
                        String.valueOf(peakHour),
                        objectMapper.writeValueAsString(new PeakHourStatsDto(peakHour))
                );
            }
        } catch (JsonProcessingException e) {
            log.error("Ошибка с сериализацией статистики", e);
        }
    }

}
