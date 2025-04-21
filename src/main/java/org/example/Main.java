package org.example;
import org.example.services.impl.WeatherServiceImpl;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        WeatherServiceImpl weatherService = new WeatherServiceImpl();
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
        weatherService.getWeatherHistory().forEach((city, temperature) ->
                System.out.println(city + ": " + temperature));

        scanner.close();
    }
}
