package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.dto.CityDto;
import com.example.dz_tinkoff.entity.CityEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public class CityRepository {

    private final JdbcTemplate jdbcTemplate;

    public CityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CityEntity> cityRowMapper = (rs, rowNum) ->
            new CityEntity(rs.getLong("id"),
                    rs.getString("name")
            );
}
