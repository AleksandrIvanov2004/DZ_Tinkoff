package com.example.dz_tinkoff.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForecastDto {
    private Long id;
    private Long cityId;
    private int temperature;
    private Timestamp date;
}
