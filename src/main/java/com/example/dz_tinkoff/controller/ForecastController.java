package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.service.WeatherService;
import com.example.dz_tinkoff.dto.ForecastDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ForecastController {

    private final WeatherService weatherService;

    public ForecastController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast/{name}")
    public void getForecast(@PathVariable String name){
        weatherService.getForecast(name);
    }
}

