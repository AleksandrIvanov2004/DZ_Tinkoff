package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CityController {

    private final WeatherService weatherService;

    public CityController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
}
