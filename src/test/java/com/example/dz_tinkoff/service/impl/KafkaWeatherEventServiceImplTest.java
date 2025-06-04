package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.WeatherRequestMetadataDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaWeatherEventServiceImplTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;


    @InjectMocks
    private KafkaWeatherEventServiceImpl kafkaWeatherEventService;

    private final String testCity = "Москва";
    private final Instant testInstant = Instant.now();
    private final WeatherRequestMetadataDto testMetadata =
            new WeatherRequestMetadataDto(testCity, testInstant);

    @Test
    void publishWeatherRequest_SuccessfullySendsToKafka() throws JsonProcessingException {
        String expectedJson = "{\"sourceApp\":\"sourceApp\",\"userId\":\"userId123\"}";
        when(objectMapper.writeValueAsString(testMetadata)).thenReturn(expectedJson);

        kafkaWeatherEventService.publishWeatherRequest(testCity, testMetadata);

        verify(kafkaTemplate).send("weather-requests", testCity, expectedJson);
        verify(objectMapper).writeValueAsString(testMetadata);
    }

    @Test
    void publishWeatherRequest_HandlesJsonProcessingException() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(testMetadata))
                .thenThrow(new JsonProcessingException("Serialization error") {});

        kafkaWeatherEventService.publishWeatherRequest(testCity, testMetadata);

        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
        verify(objectMapper).writeValueAsString(testMetadata);
    }

    @Test
    void publishWeatherRequest_WithCustomTopic() throws JsonProcessingException {
        String customTopic = "custom-topic";
        kafkaWeatherEventService.setWeatherRequestsTopic(customTopic);
        String expectedJson = "{}";
        when(objectMapper.writeValueAsString(testMetadata)).thenReturn(expectedJson);

        kafkaWeatherEventService.publishWeatherRequest(testCity, testMetadata);

        verify(kafkaTemplate).send(customTopic, testCity, expectedJson);
    }
}
