package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.dto.WeatherRequestMetadataDto;
import com.example.dz_tinkoff.service.ParseDatetimeService;
import com.example.dz_tinkoff.service.WeatherService;
import com.example.dz_tinkoff.dto.ForecastDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class ForecastController {
    private final WeatherService weatherService;
    private final ParseDatetimeService parseDatetimeService;

    @GetMapping({"/forecast/{cityName}", "/forecast/{cityName}/{dateTime}"})
    public ForecastDto getForecast(
            @PathVariable @NotBlank(message = "Название города не может быть пустым")
            @Pattern(regexp = "[а-яА-ЯёЁ\\s-]+", message = "Некорректное название города")
            String cityName,

            @PathVariable(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            String dateTime) {

        log.info("Получен запрос прогноза для города: {}", cityName);

        LocalDateTime date = (dateTime != null) ? parseDatetimeService.parseDateTime(dateTime) : LocalDateTime.now();

        LocalDateTime roundedTime = date.truncatedTo(ChronoUnit.HOURS);
        log.debug("Округлённое время: {}", roundedTime);

        ForecastDto forecast = weatherService.getForecast(cityName, roundedTime);
        log.info("Прогноз для города {} на {} успешно сформирован", cityName, roundedTime);

        return forecast;
    }
}

