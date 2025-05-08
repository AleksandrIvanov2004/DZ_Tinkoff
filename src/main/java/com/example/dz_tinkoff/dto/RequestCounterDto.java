package com.example.dz_tinkoff.dto;

import java.sql.Timestamp;

public record RequestCounterDto (Long cityId, int requestCount, Timestamp lastAccessDatetime){}
