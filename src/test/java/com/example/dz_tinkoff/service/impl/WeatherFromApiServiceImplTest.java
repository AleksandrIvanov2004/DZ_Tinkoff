package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.dto.WeatherApiResponseDto;
import com.example.dz_tinkoff.service.impl.WeatherFromApiServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherFromApiServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherFromApiServiceImpl weatherService;

    private final String testToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    @Test
    void getCachedCities_shouldReturnPredefinedSet() {
        Set<String> result = weatherService.getCachedCities();

        assertEquals(4, result.size());
        assertTrue(result.contains("Москва"));
        assertTrue(result.contains("Санкт-Петербург"));
        assertTrue(result.contains("Энгельс"));
        assertTrue(result.contains("Саратов"));
    }

    @Test
    void getWeatherFromApi_shouldBuildCorrectUrl() {
        String cityName = "Москва";
        double lat = 55.7558;
        double lon = 37.6173;
        LocalDateTime date = LocalDateTime.of(2023, 6, 15, 12, 0);

        WeatherApiResponseDto mockResponse = new WeatherApiResponseDto();
        mockResponse.setTemp2Cel(20.0);
        mockResponse.setWindSpeed10(5.0);

        ResponseEntity<WeatherApiResponseDto[]> responseEntity =
                new ResponseEntity<>(new WeatherApiResponseDto[]{mockResponse}, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(WeatherApiResponseDto[].class)))
                .thenReturn(responseEntity);

        WeatherApiResponseDto result = weatherService.getWeatherFromApi(cityName, lat, lon, date);

        assertNotNull(result);
        verify(restTemplate).getForEntity(
                argThat((String url) ->
                        url.contains("lat=55.7558") &&
                                url.contains("lon=37.6173") &&
                                url.contains("date=2023-06-15T12:00") &&
                                url.contains("token=")),
                eq(WeatherApiResponseDto[].class)
        );
    }

    @Test
    void getWeatherFromApi_shouldReturnWeatherData() {
        String cityName = "Санкт-Петербург";
        double lat = 59.9343;
        double lon = 30.3351;
        LocalDateTime date = LocalDateTime.now();

        WeatherApiResponseDto expectedResponse = new WeatherApiResponseDto();
        expectedResponse.setTemp2Cel(18.5);
        expectedResponse.setWindSpeed10(3.2);

        ResponseEntity<WeatherApiResponseDto[]> responseEntity =
                new ResponseEntity<>(new WeatherApiResponseDto[]{expectedResponse}, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(WeatherApiResponseDto[].class)))
                .thenReturn(responseEntity);

        WeatherApiResponseDto result = weatherService.getWeatherFromApi(cityName, lat, lon, date);

        assertNotNull(result);
        assertEquals(18.5, result.getTemp2Cel());
        assertEquals(3.2, result.getWindSpeed10());
    }

    @Test
    void getWeatherFromApi_shouldThrowExceptionWhenApiFails() {
        // Arrange
        String cityName = "Энгельс";
        double lat = 51.4839;
        double lon = 46.1053;
        LocalDateTime date = LocalDateTime.now();

        ResponseEntity<WeatherApiResponseDto[]> responseEntity =
                new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.getForEntity(anyString(), eq(WeatherApiResponseDto[].class)))
                .thenReturn(responseEntity);

        assertThrows(RuntimeException.class, () ->
                weatherService.getWeatherFromApi(cityName, lat, lon, date));
    }

    @Test
    void getWeatherFromApi_shouldThrowExceptionWhenEmptyResponse() {
        String cityName = "Саратов";
        double lat = 51.5336;
        double lon = 46.0343;
        LocalDateTime date = LocalDateTime.now();

        ResponseEntity<WeatherApiResponseDto[]> responseEntity =
                new ResponseEntity<>(new WeatherApiResponseDto[0], HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(WeatherApiResponseDto[].class)))
                .thenReturn(responseEntity);

        assertThrows(RuntimeException.class, () ->
                weatherService.getWeatherFromApi(cityName, lat, lon, date));
    }
}
