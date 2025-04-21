package org.example.services;

import org.example.exceptions.InvalidCityNameException;
import org.example.repositories.WeatherRepository;
import java.util.Random;


public class WeatherService {
    private static final int MIN_TEMP = -50;
    private static final int MAX_TEMP = 50;
    private final WeatherRepository weatherRepository = new WeatherRepository();
    Random random = new Random();

    private static void validateCityName(String city) throws InvalidCityNameException {
        if (city.isEmpty()) {
            throw new InvalidCityNameException("Название города не может быть пустым");
        }

        if (!city.matches("[а-яА-ЯёЁ\\s-]+")) {
            throw new InvalidCityNameException(
                    "Название города может содержать только русские буквы, пробелы и дефисы");
        }
    }

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

   public WeatherRepository getWeatherRepository(){
        return weatherRepository;
   }
}
