package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestCounterController {
    private final WeatherService weatherService;

    @GetMapping("/request_counters")
    public List<RequestCounterDto> getRequestCounters(){
        return weatherService.getRequestCounters();
    }
}
