package org.example.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherRepository {
    private static final Map<String, List<Integer>> weatherHistory = new HashMap<>();

    public void addForecast(String city, int temperature) {
        if (!weatherHistory.containsKey(city)) {
            weatherHistory.put(city, new ArrayList<>());
        }
        weatherHistory.get(city).add(temperature);
    }

    public Map<String, List<Integer>> getWeatherHistory(){
        return weatherHistory;
    }
}
