package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

@Repository
public interface ForecastRepository extends JpaRepository<ForecastEntity, Long> {

    int MIN_TEMP = -50;
    int MAX_TEMP = 50;
    Random random = new Random();


    default void getForecast(CityEntity cityEntity) {
        ForecastEntity forecast = new ForecastEntity();
        forecast.setCity(cityEntity);
        forecast.setTemperature(random.nextInt(MAX_TEMP - MIN_TEMP + 1) + MIN_TEMP);
        forecast.setDate(Timestamp.valueOf(LocalDateTime.now()));
        save(forecast);
    }
}