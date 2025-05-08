package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer> {
    @Modifying
    @Query(value = "INSERT INTO city (name) SELECT :name WHERE NOT EXISTS (SELECT 1 FROM city WHERE name = :name)",
            nativeQuery = true)
    void addCity(@Param("name") String name);


    @Query(value = "SELECT * FROM city WHERE name = :name", nativeQuery = true)
    CityEntity getCityByName(@Param("name") String name);
}