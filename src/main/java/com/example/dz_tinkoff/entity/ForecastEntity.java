package com.example.dz_tinkoff.entity;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForecastEntity {
    private Long id;
    private Long cityId;
    private int temperature;
    private Timestamp date;
}
