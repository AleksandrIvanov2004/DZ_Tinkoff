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

    @GetMapping("/cities")
    public List<CityDto> getCities(){
        return weatherService.getCities();
    }

    @GetMapping("/city/{id}")
    public CityDto getCity(@PathVariable Long id){
        return weatherService.getCity(id);
    }

    @PostMapping("/city")
    public void createCity(@RequestBody CityDto cityDto){
        weatherService.createCity(cityDto);
    }

    @DeleteMapping("/city/{id}")
    public void deleteCity(@PathVariable Long id){
        weatherService.deleteCity(id);
    }
}
