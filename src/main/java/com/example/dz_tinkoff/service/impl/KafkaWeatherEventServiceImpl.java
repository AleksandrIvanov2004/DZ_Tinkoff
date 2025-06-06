package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.WeatherRequestMetadataDto;
import com.example.dz_tinkoff.service.KafkaWeatherEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Setter
public class KafkaWeatherEventServiceImpl implements KafkaWeatherEventService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private String weatherRequestsTopic = "weather-requests";

    @Override
    public void publishWeatherRequest(String cityName, WeatherRequestMetadataDto metadata) {
        try {
            kafkaTemplate.send(
                    weatherRequestsTopic,
                    cityName,
                    objectMapper.writeValueAsString(metadata)
            );
        } catch (JsonProcessingException e) {
            log.error("Ошибка при сериализaции метаданных", e);
        }
    }
}
