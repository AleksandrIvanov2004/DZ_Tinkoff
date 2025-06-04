package com.example.dz_tinkoff.repository;

import com.example.dz_tinkoff.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@RedisHash
public interface CityRepository extends JpaRepository<CityEntity, Long> {
    @Modifying
    @Query(value = """
    INSERT INTO city (name, coord_x, coord_y) 
    SELECT :name, :coordX, :coordY 
    WHERE NOT EXISTS (SELECT 1 FROM city WHERE name = :name)
    """, nativeQuery = true)
    void addCity(
            @Param("name") String name,
            @Param("coordX") Double coordX,
            @Param("coordY") Double coordY);


    @Query(value = "SELECT * FROM city WHERE name = :name", nativeQuery = true)
    CityEntity getCityByName(@Param("name") String name);
}