package com.example.dz_tinkoff.service.impl;

import com.example.dz_tinkoff.service.ParseDatetimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Service
@Slf4j
public class ParseDatetimeServiceImpl implements ParseDatetimeService {
    @Override
    public LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(dateTimeStr);
        } catch (DateTimeParseException e) {
            log.error("Неожиданная ошибка при обработке запроса: {}", e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Неверный формат даты. Используйте ISO-формат: YYYY-MM-DDTHH:MM:SS"
            );
        }
    }
}
