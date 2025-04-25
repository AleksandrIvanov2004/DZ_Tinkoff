package org.example.repositories.impl;

import org.example.repositories.WeatherRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.sql.DataSource;

public class WeatherRepositoryImpl implements WeatherRepository {
    private final DataSource dataSource;

    public WeatherRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addForecast(String city, int temperature) {
        String citySql = """
            INSERT INTO cities (name) VALUES (?) 
            ON CONFLICT (name) DO NOTHING
            RETURNING id
            """;

        String forecastSql = "INSERT INTO forecasts (city_id, temperature, date) VALUES (?, ?, ?)";

        String counterSql = """
            INSERT INTO request_counter (city_id, request_count, last_access_datetime)
            VALUES (?, 1, CURRENT_TIMESTAMP)
            ON CONFLICT (city_id) DO UPDATE SET 
                request_count = request_counter.request_count + 1,
                last_access_datetime = CURRENT_TIMESTAMP
            """;

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int cityId;
                try (PreparedStatement cityStmt = conn.prepareStatement(citySql)) {
                    cityStmt.setString(1, city);
                    ResultSet rs = cityStmt.executeQuery();
                    if (rs.next()) {
                        cityId = rs.getInt("id");
                    }
                    else {
                        try (PreparedStatement selectStmt = conn.prepareStatement(
                                "SELECT id FROM cities WHERE name = ?")) {
                            selectStmt.setString(1, city);
                            ResultSet selectRs = selectStmt.executeQuery();
                            if (!selectRs.next()) {
                                throw new SQLException("Город не найден");
                            }
                            cityId = selectRs.getInt("id");
                        }
                    }
                }

                try (PreparedStatement forecastStmt = conn.prepareStatement(forecastSql)) {
                    forecastStmt.setInt(1, cityId);
                    forecastStmt.setInt(2, temperature);
                    forecastStmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                    forecastStmt.executeUpdate();
                }

                try (PreparedStatement counterStmt = conn.prepareStatement(counterSql)) {
                    counterStmt.setInt(1, cityId);
                    counterStmt.executeUpdate();
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public Map<String, List<Integer>> getWeatherHistory() {
        String sql = """
            SELECT c.name, f.temperature 
            FROM forecasts f
            JOIN cities c ON f.city_id = c.id
            ORDER BY f.date DESC, c.name
            LIMIT 100
            """;

        Map<String, List<Integer>> history = new LinkedHashMap<>();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String city = rs.getString("name");
                int temp = rs.getInt("temperature");
                history.computeIfAbsent(city, k -> new ArrayList<>()).add(temp);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }

        return history;
    }

    @Override
    public int getRequestCount(String city) {
        String sql = """
            SELECT rc.request_count 
            FROM request_counter rc
            JOIN cities c ON rc.city_id = c.id
            WHERE c.name = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getInt("request_count") : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }
}