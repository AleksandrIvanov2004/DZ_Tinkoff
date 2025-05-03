package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.service.WeatherService;
import com.example.dz_tinkoff.dto.ForecastDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ForecastController {

    private final WeatherService weatherService;

    @GetMapping("/forecast/{cityName}")
    public void getForecast(@PathVariable String cityName){
        weatherService.getForecast(cityName);
    }
}

