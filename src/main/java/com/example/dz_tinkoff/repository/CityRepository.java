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

    public List<CityEntity> getAll() {
        return jdbcTemplate.query("SELECT * FROM city", cityRowMapper);
    }

    public CityEntity getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM city WHERE id = ?", cityRowMapper, id);
    }

    public void save(CityEntity cityDto) {
        jdbcTemplate.update( "INSERT INTO city (name) " +
                "SELECT ? WHERE NOT EXISTS (SELECT 1 FROM city WHERE name = ?)", cityDto.getName(), cityDto.getName());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM city WHERE id =?", id);
    }
}
