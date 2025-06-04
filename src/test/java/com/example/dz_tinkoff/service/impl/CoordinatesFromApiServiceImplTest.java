package com.example.dz_tinkoff.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoordinatesFromApiServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JsonNode rootNode;

    @Mock
    private JsonNode responseNode;

    @Mock
    private JsonNode geoCollectionNode;

    @Mock
    private JsonNode featureMemberNode;

    @Mock
    private JsonNode geoObjectNode;

    @Mock
    private JsonNode pointNode;

    @Mock
    private JsonNode posNode;


    @InjectMocks
    private CoordinatesFromApiServiceImpl service;

    private final String testCity = "Москва";
    private final String apiResponse = "{\"response\":{...}}";

    @Test
    void getCityCoordinates_ReturnsCorrectCoordinates_WhenApiResponseValid() throws Exception {
        String expectedPos = "37.6176 55.7558";

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(apiResponse);

        when(objectMapper.readTree(apiResponse)).thenReturn(rootNode);
        when(rootNode.path(eq("response"))).thenReturn(responseNode);
        when(responseNode.path(eq("GeoObjectCollection"))).thenReturn(geoCollectionNode);
        when(geoCollectionNode.path(eq("featureMember"))).thenReturn(featureMemberNode);
        when(featureMemberNode.get(eq(0))).thenReturn(geoObjectNode);
        when(geoObjectNode.path(eq("GeoObject"))).thenReturn(pointNode);
        when(pointNode.path(eq("Point"))).thenReturn(posNode);
        when(posNode.path(eq("pos"))).thenReturn(posNode);
        when(posNode.isMissingNode()).thenReturn(false);
        when(posNode.asText()).thenReturn(expectedPos);

        var result = service.getCityCoordinates(testCity);

        assertAll(
                () -> assertEquals(37.6176, result.getLatitude(), 0.0001),
                () -> assertEquals(55.7558, result.getLongitude(), 0.0001)
        );
    }

    @Test
    void getCityCoordinates_ThrowsException_WhenPosNodeMissing() throws Exception {
        JsonNode posNode = mock(JsonNode.class);
        when(posNode.isMissingNode()).thenReturn(true);

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(apiResponse);
        when(objectMapper.readTree(apiResponse)).thenReturn(rootNode);
        when(rootNode.path("response")).thenReturn(responseNode);
        when(responseNode.path("GeoObjectCollection")).thenReturn(geoCollectionNode);
        when(geoCollectionNode.path("featureMember")).thenReturn(featureMemberNode);
        when(featureMemberNode.get(eq(0))).thenReturn(geoObjectNode);
        when(geoObjectNode.path(eq("GeoObject"))).thenReturn(pointNode);
        when(geoObjectNode.path("Point")).thenReturn(pointNode);
        when(pointNode.path("pos")).thenReturn(posNode);

        assertThrows(RuntimeException.class,
                () -> service.getCityCoordinates(testCity),
                "Координаты не найдены в ответе от API");
    }

    @Test
    void getCityCoordinates_ThrowsException_WhenApiRequestFails() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("API недоступно"));

        assertThrows(RuntimeException.class,
                () -> service.getCityCoordinates(testCity),
                "Ошибка при получении координат");
    }

    @Test
    void getCityCoordinates_ThrowsException_WhenInvalidJsonResponse() throws Exception {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("Неверный JSON");

        when(objectMapper.readTree("Неверный JSON"))
                .thenThrow(new RuntimeException("Неверный JSON"));

        assertThrows(RuntimeException.class,
                () -> service.getCityCoordinates(testCity));
    }
}