package com.example.dz_tinkoff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public final class WeatherRequestMetadata {
    private String city;
    private Instant requestTime;
}