package com.example.dz_tinkoff.mapper;

import com.example.dz_tinkoff.dto.ForecastDto;
import com.example.dz_tinkoff.entity.CityEntity;
import com.example.dz_tinkoff.entity.ForecastEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ForecastMapper {

    @Mapping(source = "city.id", target = "cityId")
    ForecastDto mapToDto(ForecastEntity entity);

    @Mapping(target = "city", source = "cityId", qualifiedByName = "idToCity")
    ForecastEntity mapToEntity(ForecastDto dto);

    @Named("idToCity")
    default CityEntity mapCityIdToCity(Long cityId) {
        if (cityId == null) {
            return null;
        }
        CityEntity city = new CityEntity();
        city.setId(cityId);
        return city;
    }
}