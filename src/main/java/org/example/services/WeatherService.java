package org.example.services;

import org.example.exceptions.InvalidCityNameException;

import java.util.List;
import java.util.Map;

public interface WeatherService {
    void processForecast(String city);
    Map<String, List<Integer>> getWeatherHistory();
    void validateCityName(String city) throws InvalidCityNameException;
    int getRequestCount(String city);
}
