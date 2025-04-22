package org.example.repositories.impl;

import org.example.repositories.WeatherRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherRepositoryImpl implements WeatherRepository {
    private static final Map<String, List<Integer>> weatherHistory = new HashMap<>();

    @Override
    public void addForecast(String city, int temperature) {
        if (!weatherHistory.containsKey(city)) {
            weatherHistory.put(city, new ArrayList<>());
        }
        weatherHistory.get(city).add(temperature);
    }

    @Override
    public Map<String, List<Integer>> getWeatherHistory(){
        return weatherHistory;
    }
}
