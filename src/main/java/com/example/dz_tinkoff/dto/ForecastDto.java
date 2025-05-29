package com.example.dz_tinkoff.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public record ForecastDto(Long id, Long cityId, int temperature, Timestamp date)
        implements Serializable {

    private static final long serialVersionUID = 1L; // Версия для контроля совместимости
}