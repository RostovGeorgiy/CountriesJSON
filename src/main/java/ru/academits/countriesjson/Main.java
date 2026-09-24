package ru.academits.countriesjson;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.core.JacksonException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream countriesInputStream = Main.class.getClassLoader().getResourceAsStream("countries.json")) {
            if (countriesInputStream == null) {
                System.err.println("Критическая ошибка: Файл countries.json не найден в папке resources!");
                return;
            }

            List<Country> countries = mapper.readValue(countriesInputStream, new TypeReference<>() {
            });

            long allCountriesPopulation = countries.stream()
                    .mapToLong(Country::getPopulation)
                    .sum();

            List<String> currenciesList = countries.stream()
                    .filter(c -> c.getCurrencies() != null)
                    .flatMap(c -> c.getCurrencies().stream())
                    .map(Currency::getName)
                    .filter(name -> name != null && !name.isEmpty())
                    .distinct()
                    .toList();

            System.out.println("Total population in all countries: " + allCountriesPopulation);
            System.out.println("List of all currencies: " + currenciesList);

            List<Country> oneMillionPlusCountries = countries.stream()
                    .filter(c -> c.getPopulation() >= 1000000)
                    .collect(Collectors.toList());

            ObjectNode newRootNode = mapper.createObjectNode();
            newRootNode.putPOJO("countriesWithPopulationOfNotLessThanOneMillion", oneMillionPlusCountries);

            File outputFile = new File("output.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, newRootNode);

            System.out.println("JSON файл успешно создан.");

        } catch (JacksonException e) {
            System.err.println("Ошибка JSON: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}