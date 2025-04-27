package org.example.repositories.impl;

import org.example.repositories.WeatherRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.sql.DataSource;

public class WeatherRepositoryImpl implements WeatherRepository {
    private final DataSource dataSource;
    private final String citySql = """
            INSERT INTO cities (name) VALUES (?) 
            ON CONFLICT (name) DO NOTHING
            RETURNING id
            """;
    private final String forecastSql = "INSERT INTO forecasts (city_id, temperature, date) VALUES (?, ?, ?)";

    private final String counterSql = """
            INSERT INTO request_counter (city_id, request_count, last_access_datetime)
            VALUES (?, 1, CURRENT_TIMESTAMP)
            ON CONFLICT (city_id) DO UPDATE SET 
                request_count = request_counter.request_count + 1,
                last_access_datetime = CURRENT_TIMESTAMP
            """;

    private final String forecastsAndCitiesSql = """
            SELECT c.name, f.temperature 
            FROM forecasts f
            JOIN cities c ON f.city_id = c.id
            ORDER BY f.date DESC, c.name
            LIMIT 100
            """;

    private final String countSql = """
            SELECT rc.request_count 
            FROM request_counter rc
            JOIN cities c ON rc.city_id = c.id
            WHERE c.name = ?
            """;

    private final String findCity = "SELECT id FROM cities WHERE name = ?";

    public WeatherRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int getCityId(Connection conn, String city) {
        int cityId;
            try (PreparedStatement cityStmt = conn.prepareStatement(citySql)) {
                cityStmt.setString(1, city);
                ResultSet rs = cityStmt.executeQuery();
                if (rs.next()) {
                    cityId = rs.getInt("id");
                    return cityId;
                } else {
                    try (PreparedStatement selectStmt = conn.prepareStatement(
                            findCity)) {
                        selectStmt.setString(1, city);
                        ResultSet selectRs = selectStmt.executeQuery();
                        if (!selectRs.next()) {
                            throw new SQLException("Город не найден");
                        }
                        cityId = selectRs.getInt("id");
                        return cityId;
                    }
                }
            } catch (SQLException e){
                throw new RuntimeException("Database error", e);
            }

    }

    @Override
    public void insertForecast(Connection conn, String city, int temperature){
            try (PreparedStatement forecastStmt = conn.prepareStatement(forecastSql)) {
                forecastStmt.setInt(1, getCityId(conn, city));
                forecastStmt.setInt(2, temperature);
                forecastStmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                forecastStmt.executeUpdate();
            } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public void countStmt(Connection conn, String city) {
            try (PreparedStatement counterStmt = conn.prepareStatement(counterSql)) {
                counterStmt.setInt(1, getCityId(conn, city));
                counterStmt.executeUpdate();
            } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
            }
    }

    @Override
    public void addForecast(String city, int temperature) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try {
                insertForecast(conn, city, temperature);
                countStmt(conn, city);

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
        Map<String, List<Integer>> history = new LinkedHashMap<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(forecastsAndCitiesSql);
             ResultSet rs = stmt.executeQuery()) {

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
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(countSql)) {

            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getInt("request_count") : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }
}