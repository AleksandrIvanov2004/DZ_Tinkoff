package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
@RedisHash
public interface ForecastRepository extends JpaRepository<ForecastEntity, Long> {

    default ForecastEntity getForecast(CityEntity cityEntity, int temperature) {
        ForecastEntity forecast = new ForecastEntity();
        forecast.setCity(cityEntity);
        forecast.setTemperature(temperature);
        forecast.setDate(Timestamp.valueOf(LocalDateTime.now()));
        save(forecast);
        return forecast;
    }
}