package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RequestCounterRepository extends JpaRepository<RequestCounterEntity, Long> {
    List<RequestCounterEntity> findAll();

    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO request_counter(city_id, request_count, last_access_datetime) " +
                    "VALUES (:#{#city.id}, 1, CURRENT_TIMESTAMP) " +
                    "ON CONFLICT (city_id) DO UPDATE SET " +
                    "request_count = request_counter.request_count + 1, " +
                    "last_access_datetime = CURRENT_TIMESTAMP")
    void updateRequestsByCityId(@Param("city") CityEntity city);
}