package com.example.dz_tinkoff.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class WeatherApiResponseDto {
    @JsonProperty("temp_2_cel")
    private Double temp2Cel;

    @JsonProperty("wind_speed_10")
    private Double windSpeed10;
}
