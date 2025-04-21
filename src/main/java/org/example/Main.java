package org.example;
import org.example.services.WeatherService;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        Scanner scanner = new Scanner(System.in, "UTF-8");

        while (true) {
                System.out.print("\nВведите название города (или 'выход' для завершения): ");
                String city = scanner.nextLine().trim();

                if (city.equalsIgnoreCase("выход")) {
                    break;
                }

               weatherService.processForecast(city);

        }

        System.out.println("\nИстория запросов:");
        weatherService.getWeatherRepository().getWeatherHistory().forEach((city, temperature) ->
                System.out.println(city + ": " + temperature));

        scanner.close();
    }
}
