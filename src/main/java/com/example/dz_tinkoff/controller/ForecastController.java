package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.entity.ForecastEntity;
import com.example.dz_tinkoff.service.WeatherService;
import com.example.dz_tinkoff.dto.ForecastDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ForecastController {

    private final WeatherService weatherService;

    @GetMapping("/forecast/{cityName}")
    public ForecastDto getForecast(@PathVariable @NotBlank(message = "Название города не может быть пустым")
                                @Pattern(regexp = "[а-яА-ЯёЁ\\s-]+",
            message = "Некорректное название города") String cityName){
        return weatherService.getForecast(cityName);
    }
}

