package com.example.dz_tinkoff.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RequestCounterEntity {
    private Long city_id;
    private int request_count;
    private Timestamp last_access_datetime;

    public RequestCounterEntity(Long city_id, int request_count, Timestamp last_access_datetime) {
        this.city_id = city_id;
        this.request_count = request_count;
        this.last_access_datetime = last_access_datetime;
    }

    public Long getCityId() {
        return city_id;
    }

    public int getRequestCount() {
        return request_count;
    }

    public Timestamp getLastAccessDatetime() { return last_access_datetime; }

    public void setCityId(Long city_id) {
        this.city_id = city_id;
    }

    public void setRequestCount(int request_count) {
        this.request_count = request_count;
    }

    public void setLastAccessDatetime(Timestamp last_access_datetime) {
        this.last_access_datetime = last_access_datetime;
    }
}
