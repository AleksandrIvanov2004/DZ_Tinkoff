package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.ForecastEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequestCounterRepository {
    private final JdbcTemplate jdbcTemplate;

    public RequestCounterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RequestCounterEntity> requestCounterEntityRowMapper = (rs, rowNum) ->
            new RequestCounterEntity(rs.getLong("city_id"),
                    rs.getInt("request_count"),
                    rs.getTimestamp("last_access_datetime")
            );

    public List<RequestCounterEntity> getAll() {
        return jdbcTemplate.query("SELECT * FROM request_counter", requestCounterEntityRowMapper);
    }

    public RequestCounterEntity getByCityId(Long city_id) {
        return jdbcTemplate.queryForObject("SELECT * FROM request_counter WHERE city_id = ?",
                requestCounterEntityRowMapper, city_id);
    }
}
