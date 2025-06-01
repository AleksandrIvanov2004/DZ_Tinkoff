package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.entity.ForecastEntity;
import com.example.dz_tinkoff.service.WeatherService;
import com.example.dz_tinkoff.dto.ForecastDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class ForecastController {

    private final WeatherService weatherService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping("/forecast/{cityName}")
    public ForecastDto getForecast(@PathVariable @NotBlank(message = "Название города не может быть пустым")
                                @Pattern(regexp = "[а-яА-ЯёЁ\\s-]+",
            message = "Некорректное название города") String cityName){
        ForecastDto forecast = weatherService.getForecast(cityName);

        return forecast;
    }

}

