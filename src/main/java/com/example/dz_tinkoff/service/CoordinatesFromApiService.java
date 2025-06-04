package com.example.dz_tinkoff.service;

import com.example.dz_tinkoff.dto.CityCoordinatesDto;

public interface CoordinatesFromApiService {
    CityCoordinatesDto getCityCoordinates(String cityName);
}
