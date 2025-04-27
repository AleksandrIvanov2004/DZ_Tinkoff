package com.example.dz_tinkoff.entity;

import java.sql.Timestamp;
import java.util.Date;

public class ForecastEntity {
    private Long id;
    private Long city_id;
    private int temperature;
    private Timestamp date;

    public ForecastEntity(Long id, Long city_id, int temperature, Timestamp date) {
        this.id = id;
        this.city_id = city_id;
        this.temperature = temperature;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Long getCityId() {
        return city_id;
    }

    public int getTemperature() {
        return temperature;
    }

    public Timestamp getDate() { return date; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCityId(Long city_id) {
        this.city_id = city_id;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
