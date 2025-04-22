package org.example.services.impl;

import org.example.exceptions.InvalidCityNameException;
import org.example.repositories.impl.WeatherRepositoryImpl;
import org.example.repositories.WeatherRepository;
import org.example.services.WeatherService;

import java.util.List;
import java.util.Map;
import java.util.Random;


public class WeatherServiceImpl implements WeatherService {
    private static final int MIN_TEMP = -50;
    private static final int MAX_TEMP = 50;
    private final WeatherRepository weatherRepository = new WeatherRepositoryImpl();
    Random random = new Random();

   @Override
   public void processForecast(String city){
        try {
            validateCityName(city);
            int temperature = random.nextInt(MAX_TEMP - MIN_TEMP + 1) + MIN_TEMP;

            System.out.println("\nТемпература в городе " + city + ": " + temperature);
            weatherRepository.addForecast(city, temperature);
        } catch (InvalidCityNameException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

   }

   @Override
   public Map<String, List<Integer>> getWeatherHistory(){
       return weatherRepository.getWeatherHistory();
   }

   @Override
   public void validateCityName(String city) throws InvalidCityNameException {
        if (city.isEmpty()) {
            throw new InvalidCityNameException("Название города не может быть пустым");
        }

        if (!city.matches("[а-яА-ЯёЁ\\s-]+")) {
            throw new InvalidCityNameException(
                    "Название города может содержать только русские буквы, пробелы и дефисы");
        }
   }
}
