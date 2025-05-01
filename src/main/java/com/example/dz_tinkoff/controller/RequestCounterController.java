package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestCounterController {
    private final WeatherService weatherService;

    public RequestCounterController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/request_counters")
    public List<RequestCounterDto> getRequestCounters(){
        return weatherService.getRequestCounters();
    }
}
