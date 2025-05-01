package com.example.dz_tinkoff.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestCounterDto {
    private Long cityId;
    private int requestCount;
    private Timestamp lastAccessDatetime;
}
