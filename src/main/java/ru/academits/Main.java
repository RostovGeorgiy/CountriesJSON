package ru.academits;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;

        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("countries.json")) {
            if (is == null) {
                System.err.println("Критическая ошибка: Файл countries.json не найден в папке resources!");
                return;
            }
            rootNode = mapper.readTree(is);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении исходного JSON файла:");
            e.printStackTrace();
            return;
        }

        List<JsonNode> countryObjectsList = new ArrayList<>();
        long allCountriesPopulation = 0;
        List<String> currenciesList = new ArrayList<>();
        List<JsonNode> oneMillionPlusPopulationCountries = new ArrayList<>();

        if (rootNode.isArray()) {
            for (JsonNode countryNode : rootNode) {
                countryObjectsList.add(countryNode);

                int countryPopulation = countryNode.path("population").asInt(0);
                allCountriesPopulation += countryPopulation;

                if (countryPopulation >= 1000000) {
                    oneMillionPlusPopulationCountries.add(countryNode);
                }

                JsonNode countryCurrencies = countryNode.path("currencies");

                if (countryCurrencies.isArray()) {
                    for (JsonNode currency : countryCurrencies) {
                        String currencyName = currency.path("name").asString();

                        if (!currencyName.isEmpty() && !currenciesList.contains(currencyName)) {
                            currenciesList.add(currencyName);
                        }
                    }
                }
            }

            System.out.println("Total population in all countries: " + allCountriesPopulation);
            System.out.println("List of all currencies: " + currenciesList);

        } else {
            System.out.println("Expected a JSON array at the root element.");
            return;
        }

        try {
            ObjectNode newRootNode = mapper.createObjectNode();

            ArrayNode oneMillionPlusPopulationCountriesArray = mapper.createArrayNode();
            oneMillionPlusPopulationCountriesArray.addAll(oneMillionPlusPopulationCountries);
            newRootNode.set("countriesWithPopulationOfNotLessThanOneMillion", oneMillionPlusPopulationCountriesArray);

            File outputFile = new File("output.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, newRootNode);

            System.out.println("JSON файл успешно создан (output.json)!");

        } catch (tools.jackson.core.JacksonException e) {
            System.err.println("Ошибка при записи выходного JSON файла:");
            e.printStackTrace();
        }
    }
}