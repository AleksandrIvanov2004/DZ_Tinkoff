package com.example.dz_tinkoff.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherStatsResponse {
    private PopularCityStatsDto popularCity;
    private PeakHourStatsDto peakHour;
}
