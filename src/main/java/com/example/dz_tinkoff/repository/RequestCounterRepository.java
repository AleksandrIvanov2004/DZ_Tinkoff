package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.RequestCounterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RedisHash
public interface RequestCounterRepository extends JpaRepository<RequestCounterEntity, Long> {
    List<RequestCounterEntity> findAll();


    @Query("SELECT c.name FROM request_counter rc " +
            "JOIN rc.city c " +
            "WHERE rc.lastAccessDatetime >= :monthAgo " +
            "ORDER BY rc.requestCount DESC " +  // Сортировка по полю request_count
            "LIMIT 1")
    Optional<String> findMostPopularCityLastMonth(@Param("monthAgo") Timestamp monthAgo);

    @Query("SELECT EXTRACT(HOUR FROM rc.lastAccessDatetime) as hour " +
            "FROM request_counter rc " +
            "WHERE rc.lastAccessDatetime >= :monthAgo " +
            "GROUP BY hour " +
            "ORDER BY COUNT(rc) DESC " +
            "LIMIT 1")
    Optional<Integer> findPeakHourLastMonth(@Param("monthAgo") Timestamp monthAgo);
}