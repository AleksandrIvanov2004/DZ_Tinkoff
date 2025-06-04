package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.service.ParseDatetimeService;
import com.example.dz_tinkoff.service.WeatherService;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForecastControllerTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private ParseDatetimeService parseDatetimeService;

    @InjectMocks
    private ForecastController forecastController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<ConstraintViolation<ForecastController>> validateParameters(String cityName, String dateTime) throws NoSuchMethodException {
        return validator.forExecutables()
                .validateParameters(
                        forecastController,
                        ForecastController.class.getMethod("getForecast", String.class, String.class),
                        new Object[]{cityName, dateTime}
                );
    }

    @Test
    void getForecast_ValidCityNameWithoutDateTime_CallsServiceAndReturnsResult() {
        String validCityName = "Москва";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ForecastDto expectedDto = new ForecastDto(null, null, 0.0, null, 0.0);

        when(weatherService.getForecast(validCityName, now)).thenReturn(expectedDto);

        ForecastDto result = forecastController.getForecast(validCityName, null);

        verify(weatherService, times(1)).getForecast(validCityName, now);
        assertEquals(expectedDto, result);
    }

    @Test
    void getForecast_ValidCityNameWithDateTime_CallsServiceAndReturnsResult() {
        String validCityName = "Москва";
        String dateTime = "2025-06-10T15:30";
        LocalDateTime parsedDateTime = LocalDateTime.parse("2025-06-10T15:00");
        ForecastDto expectedDto = new ForecastDto(null, null, 0.0, null, 0.0);

        when(parseDatetimeService.parseDateTime(dateTime)).thenReturn(LocalDateTime.parse("2025-06-10T15:30"));
        when(weatherService.getForecast(validCityName, parsedDateTime)).thenReturn(expectedDto);

        ForecastDto result = forecastController.getForecast(validCityName, dateTime);

        verify(parseDatetimeService, times(1)).parseDateTime(dateTime);
        verify(weatherService, times(1)).getForecast(validCityName, parsedDateTime);
        assertEquals(expectedDto, result);
    }

    @Test
    void getForecast_ShouldPassValidation_WhenValidCityName() throws NoSuchMethodException {
        String validCity = "Москва";

        Set<ConstraintViolation<ForecastController>> violations = validateParameters(validCity, null);

        assertTrue(violations.isEmpty());
    }

    @Test
    void getForecast_ShouldFailValidation_WhenIncorrectCityName() throws NoSuchMethodException {
        String invalidCity = "fm58";

        Set<ConstraintViolation<ForecastController>> violations = validateParameters(invalidCity, null);

        assertFalse(violations.isEmpty());
        assertEquals("Некорректное название города", violations.iterator().next().getMessage());
    }

    @Test
    void getForecast_ShouldUseCurrentTime_WhenDateTimeIsNull() {
        String cityName = "Москва";
        LocalDateTime expectedTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ForecastDto expectedDto = new ForecastDto(null, null, 0.0, null, 0.0);

        when(weatherService.getForecast(cityName, expectedTime)).thenReturn(expectedDto);

        ForecastDto result = forecastController.getForecast(cityName, null);

        verify(weatherService).getForecast(cityName, expectedTime);
        assertEquals(expectedDto, result);
    }

    @Test
    void getForecast_ShouldUseParsedDateTime_WhenDateTimeIsProvided() {
        // Arrange
        String cityName = "Санкт-Петербург";
        String dateTimeString = "2025-06-10T15:30";
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime expectedRoundedTime = parsedDateTime.truncatedTo(ChronoUnit.HOURS);
        ForecastDto expectedDto = new ForecastDto(null, null, 0.0, null, 0.0);

        when(parseDatetimeService.parseDateTime(dateTimeString)).thenReturn(parsedDateTime);
        when(weatherService.getForecast(cityName, expectedRoundedTime)).thenReturn(expectedDto);

        ForecastDto result = forecastController.getForecast(cityName, dateTimeString);

        verify(parseDatetimeService).parseDateTime(dateTimeString);
        verify(weatherService).getForecast(cityName, expectedRoundedTime);
        assertEquals(expectedDto, result);
    }
}