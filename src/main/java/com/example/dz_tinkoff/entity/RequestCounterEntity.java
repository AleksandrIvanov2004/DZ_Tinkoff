package com.example.dz_tinkoff.entity;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "request_counter")
public class RequestCounterEntity {
    @Id
    @Column(name = "city_id", columnDefinition = "bigserial")
    private Long cityId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;

    @Column(name = "request_count")
    private int requestCount;
    @Column(name = "last_access_datetime")
    private Timestamp lastAccessDatetime;
}
