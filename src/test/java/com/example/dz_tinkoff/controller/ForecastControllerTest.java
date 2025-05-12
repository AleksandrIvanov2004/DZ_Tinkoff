package com.example.dz_tinkoff.controller;

import com.example.dz_tinkoff.dto.ForecastDto;
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


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ForecastControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private ForecastController forecastController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<ConstraintViolation<ForecastController>> valide(String cityName) throws NoSuchMethodException {
        return validator
                .forExecutables()
                .validateParameters(
                        forecastController,
                        ForecastController.class.getMethod("getForecast", String.class),
                        new Object[]{cityName}
                );
    }

    @Test
    void getForecastValidCityNameCallsServiceAndReturnsResult() {
        String validCityName = "Москва";
        ForecastDto expectedDto = new ForecastDto(null, null, 0, null);
        when(weatherService.getForecast(validCityName)).thenReturn(expectedDto);

        ForecastDto result = forecastController.getForecast(validCityName);

        verify(weatherService, times(1)).getForecast(validCityName);
        assertEquals(expectedDto, result);
    }

    @Test
    void getForecastShouldPassValidation() throws NoSuchMethodException {
        String validCity = "Москва";

        Set<ConstraintViolation<ForecastController>> violations = valide(validCity);

        assertTrue(violations.isEmpty());
    }

    @Test
    void getForecastShouldFailValidationIncoerrectCityName() throws NoSuchMethodException {
        String invalidCity = "fm58";

        Set<ConstraintViolation<ForecastController>> violations = valide(invalidCity);

        assertFalse(violations.isEmpty());
        assertEquals("Некорректное название города", violations.iterator().next().getMessage());
    }

    @Test
    void getForecastShouldFailValidationEmptyCityName() throws NoSuchMethodException {
        String emptyCity = "";

        Set<ConstraintViolation<ForecastController>> violations = valide(emptyCity);

        assertFalse(violations.isEmpty());
        assertEquals("Название города не может быть пустым", violations.iterator().next().getMessage());
    }
}