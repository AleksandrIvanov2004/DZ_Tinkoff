package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.dto.PeakHourStatsDto;
import com.example.dz_tinkoff.dto.PopularCityStatsDto;
import java.util.Optional;

public interface WeatherStatsConsumerService {
    PopularCityStatsDto consumePopularCityStats(String message);
    PeakHourStatsDto consumePeakHourStats(String message);

    Optional<PopularCityStatsDto> getLastPopularCityStats();
    Optional<PeakHourStatsDto> getLastPeakHourStats();
}
