package ru.academits;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("countries.json")) {
            if (is == null) {
                System.out.println("Файл countries.json не найден!");
                return;
            }

            // Читаем массив как список объектов Country
            List<Country> countries = mapper.readValue(is, new TypeReference<List<Country>>() {});

            System.out.println("Успешно загружено стран: " + countries.size());

            // Выведем информацию о первой стране (Ангилья)
            if (!countries.isEmpty()) {
                Country first = countries.getFirst();
                System.out.println("Название: " + first.getName());
                System.out.println("Столица: " + first.getCapital());
                System.out.println("Валюта: " + first.getCurrencies().getFirst().getName());
                System.out.println("Перевод (DE): " + first.getTranslations().getDe());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}