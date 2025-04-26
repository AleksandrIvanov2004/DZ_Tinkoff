package org.example.repositories;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface WeatherRepository {
     void addForecast(String city, int temperature);
     Map<String, List<Integer>> getWeatherHistory();
     int getRequestCount(String city);
     int getCityId(Connection conn, String city);
     void insertForecast(Connection conn, String city, int temperature);
     void countStmt(Connection conn, String city);
}
