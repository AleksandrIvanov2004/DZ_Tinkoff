package org.example;
import java.util.*;

class InvalidCityNameException extends Exception {
    public InvalidCityNameException(String message) {
        super(message);
    }
}

public class Main {
    private static final int MIN_TEMP = -50;
    private static final int MAX_TEMP = 50;
    private static final Map<String, List<Integer>> weatherHistory = new HashMap<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        Random random = new Random();

        while (true) {
            try {
                System.out.print("\nВведите название города (или 'выход' для завершения): ");
                String city = scanner.nextLine().trim();

                if (city.equalsIgnoreCase("выход")) {
                    break;
                }

                validateCityName(city);
                int temperature = random.nextInt(MAX_TEMP - MIN_TEMP + 1) + MIN_TEMP;

                System.out.println("\nТемпература в городе " + city + ": " + temperature);
                if (!weatherHistory.containsKey(city)) {
                    weatherHistory.put(city, new ArrayList<>());
                }
                weatherHistory.get(city).add(temperature);

            } catch (InvalidCityNameException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        System.out.println("\nИстория запросов:");
        weatherHistory.forEach((city, temperature) ->
                System.out.println(city + ": " + temperature));

        scanner.close();
    }

    private static void validateCityName(String city) throws InvalidCityNameException {
        if (city.isEmpty()) {
            throw new InvalidCityNameException("Название города не может быть пустым");
        }

        if (!city.matches("[а-яА-ЯёЁ\\s-]+")) {
            throw new InvalidCityNameException(
                    "Название города может содержать только русские буквы, пробелы и дефисы");
        }
    }
}
