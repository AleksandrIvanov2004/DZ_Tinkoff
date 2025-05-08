package com.example.dz_tinkoff.dto;

import java.sql.Timestamp;

public record ForecastDto (Long id, Long cityId, int temperature, Timestamp date){}


