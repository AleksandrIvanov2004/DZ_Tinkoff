package com.example.dz_tinkoff.entity;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestCounterEntity {
    private Long cityId;
    private int requestCount;
    private Timestamp lastAccessDatetime;
}
