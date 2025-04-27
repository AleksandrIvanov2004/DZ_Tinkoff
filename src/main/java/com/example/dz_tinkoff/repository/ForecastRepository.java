package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ForecastRepository {
    private final JdbcTemplate jdbcTemplate;

    public ForecastRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ForecastEntity> forecastRowMapper = (rs, rowNum) ->
            new ForecastEntity(rs.getLong("id"),
                    rs.getLong("city_id"),
                    rs.getInt("temperature"),
                    rs.getTimestamp("date")
            );

    public List<ForecastEntity> getAll() {
        return jdbcTemplate.query("SELECT * FROM forecast", forecastRowMapper);
    }

    public ForecastEntity getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM forecast WHERE id = ?", forecastRowMapper, id);
    }

    public void save(ForecastEntity forecastEntity) {
        jdbcTemplate.update("INSERT INTO forecast (city_id, temperature, date) VALUES (?, ?, ?) ", forecastEntity.getCityId(),
                forecastEntity.getTemperature(), LocalDateTime.now());

        jdbcTemplate.update(
                "UPDATE request_counter SET request_count = request_count + 1, " +
                        "last_access_datetime = CURRENT_TIMESTAMP WHERE city_id = ?",
                forecastEntity.getCityId());

        jdbcTemplate.update(
                "INSERT INTO request_counter(city_id, request_count, last_access_datetime) " +
                        "SELECT ?, 1, CURRENT_TIMESTAMP WHERE NOT EXISTS " +
                        "(SELECT 1 FROM request_counter WHERE city_id = ?)",
                forecastEntity.getCityId(), forecastEntity.getCityId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM forecast WHERE id =?", id);
    }
}
