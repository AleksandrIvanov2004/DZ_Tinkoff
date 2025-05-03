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

    // Здесь наверное нужно name, в таблице city же поле name, но я не уверен
    public void addCity(String name){
        jdbcTemplate.update( "INSERT INTO city (name) " +
                "SELECT ? WHERE NOT EXISTS (SELECT 1 FROM city WHERE name = ?)", name, name);

    }

    public long getCityIdByName(String name){
        return jdbcTemplate.queryForObject("SELECT id FROM city WHERE name = ?", Long.class, name);
    }
}
