package com.example.dz_tinkoff.controller;
import com.example.dz_tinkoff.dto.RequestCounterDto;
import com.example.dz_tinkoff.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestCounterControllerTest {
    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private RequestCounterController requestCounterController;

    @Test
    void getRequestCounters_ShouldReturnListFromService() {
        Timestamp currentTime = Timestamp.from(Instant.now());
        List<RequestCounterDto> expectedCounters = List.of(
                new RequestCounterDto(1L, 5, currentTime),
                new RequestCounterDto(2L, 3, currentTime)
        );

        when(weatherService.getRequestCounters()).thenReturn(expectedCounters);

        List<RequestCounterDto> actualCounters = requestCounterController.getRequestCounters();

        assertEquals(expectedCounters, actualCounters);
        assertEquals(2, actualCounters.size());
    }

    @Test
    void getRequestCounters_WhenEmpty_ShouldReturnEmptyList() {
        when(weatherService.getRequestCounters()).thenReturn(List.of());

        List<RequestCounterDto> result = requestCounterController.getRequestCounters();

        assertEquals(0, result.size());
    }
}
