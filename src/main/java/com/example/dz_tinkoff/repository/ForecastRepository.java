package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.Random;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ForecastRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final int MIN_TEMP = -50;
    private static final int MAX_TEMP = 50;
    Random random = new Random();

    public ForecastRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ForecastEntity> forecastRowMapper = (rs, rowNum) ->
            new ForecastEntity(rs.getLong("id"),
                    rs.getLong("city_id"),
                    rs.getInt("temperature"),
                    rs.getTimestamp("date")
            );

    public void getByCityId(long cityId) {
        jdbcTemplate.update("INSERT INTO forecast (city_id, temperature, date) VALUES (?, ?, ?) ", cityId,
                random.nextInt(MAX_TEMP - MIN_TEMP + 1) + MIN_TEMP, LocalDateTime.now());

    }
}
