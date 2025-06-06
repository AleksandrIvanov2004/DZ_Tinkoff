package com.example.dz_tinkoff.service.impl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParseDatetimeServiceImplTest {

    @InjectMocks
    private ParseDatetimeServiceImpl parseDatetimeService;

    @Test
    void parseDateTime_ReturnsCurrentDateTime_WhenInputIsNull() {
        LocalDateTime result = parseDatetimeService.parseDateTime(null);

        assertNotNull(result);
        assertTrue(Math.abs(LocalDateTime.now().getSecond() - result.getSecond()) <= 1);
    }

    @Test
    void parseDateTime_ReturnsCorrectDateTime_WhenValidIsoFormat() {
        String validDateTime = "2023-05-15T14:30:00";

        LocalDateTime result = parseDatetimeService.parseDateTime(validDateTime);

        assertEquals(LocalDateTime.of(2023, 5, 15, 14, 30, 0), result);
    }

    @Test
    void parseDateTime_ThrowsResponseStatusException_WhenInvalidFormat() {
        String invalidDateTime = "15-05-2023 14:30:00";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> parseDatetimeService.parseDateTime(invalidDateTime));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Неверный формат даты. Используйте ISO-формат: YYYY-MM-DDTHH:MM:SS",
                exception.getReason());
    }

    @Test
    void parseDateTime_ThrowsResponseStatusException_WhenEmptyString() {
        String emptyDateTime = "";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> parseDatetimeService.parseDateTime(emptyDateTime));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void parseDateTime_HandlesEdgeCases() {
        assertDoesNotThrow(() -> parseDatetimeService.parseDateTime("0001-01-01T00:00:00"));
        assertDoesNotThrow(() -> parseDatetimeService.parseDateTime("2024-02-29T00:00:00"));
    }
}
