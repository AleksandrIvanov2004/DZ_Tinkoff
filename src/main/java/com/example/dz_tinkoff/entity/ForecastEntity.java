package com.example.dz_tinkoff.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "forecast")
public class ForecastEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityEntity city;

    private int temperature;
    private Timestamp date;
}
