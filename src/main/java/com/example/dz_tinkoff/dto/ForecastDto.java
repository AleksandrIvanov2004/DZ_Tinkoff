package com.example.dz_tinkoff.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public record ForecastDto(Long id, Long cityId, Double temperature, Timestamp date, Double windSpeed)
        implements Serializable {

    private static final long serialVersionUID = 1L; // Версия для контроля совместимости
}