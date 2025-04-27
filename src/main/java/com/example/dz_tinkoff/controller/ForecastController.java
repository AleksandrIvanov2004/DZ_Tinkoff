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

    @GetMapping("/forecasts")
    public List<ForecastDto> getForecasts(){
        return weatherService.getForecasts();
    }

    @GetMapping("/forecast/{id}")
    public ForecastDto getForecast(@PathVariable Long id){
        return weatherService.getForecast(id);
    }

    @PostMapping("/forecast")
    public void createForecast(@RequestBody ForecastDto forecastDto){
        weatherService.createForecast(forecastDto);
    }

    @DeleteMapping("/forecast/{id}")
    public void deleteForecast(@PathVariable Long id){
        weatherService.deleteForecast(id);
    }
}

