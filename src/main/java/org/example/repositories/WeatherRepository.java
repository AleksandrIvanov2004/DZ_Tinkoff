package org.example.repositories;

import java.util.List;
import java.util.Map;

public interface WeatherRepository {
     void addForecast(String city, int temperature);
     Map<String, List<Integer>> getWeatherHistory();
     int getRequestCount(String city);
}
