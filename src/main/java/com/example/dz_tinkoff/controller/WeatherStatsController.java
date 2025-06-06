package com.example.dz_tinkoff.controller;


import com.example.dz_tinkoff.dto.PeakHourStatsDto;
import com.example.dz_tinkoff.dto.PopularCityStatsDto;
import com.example.dz_tinkoff.dto.WeatherStatsResponse;
import com.example.dz_tinkoff.service.WeatherStatsConsumerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Weather Statistics API", description = "API для получения статистики запросов погоды")
@RestController
@RequestMapping("/api/v1/weather-stats")
@RequiredArgsConstructor
public class WeatherStatsController {

    private final WeatherStatsConsumerService statsService;

    @Operation(summary = "Получить самый популярный город")
    @GetMapping("/popular-city")
    public ResponseEntity<PopularCityStatsDto> getPopularCity() {
        return statsService.getLastPopularCityStats()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Получить пиковый час запросов")
    @GetMapping("/peak-hour")
    public ResponseEntity<PeakHourStatsDto> getPeakHour() {
        return statsService.getLastPeakHourStats()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Получить всю статистику")
    @GetMapping
    public ResponseEntity<WeatherStatsResponse> getAllStats() {
        PopularCityStatsDto popularCity = statsService.getLastPopularCityStats().orElse(null);
        PeakHourStatsDto peakHour = statsService.getLastPeakHourStats().orElse(null);

        if (popularCity == null && peakHour == null) {
            return ResponseEntity.noContent().build();
        }

        WeatherStatsResponse response = WeatherStatsResponse.builder()
                .popularCity(popularCity)
                .peakHour(peakHour)
                .build();

        return ResponseEntity.ok(response);
    }
}
