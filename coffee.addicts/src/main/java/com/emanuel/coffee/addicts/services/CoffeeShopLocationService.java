package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoffeeShopLocationService {
    private static final String CSV_URL = "https://raw.githubusercontent.com/Agilefreaks/test_oop/master/coffee_shops.csv";
    private static final Logger logger = LoggerFactory.getLogger(CoffeeShopLocationService.class);
    public List<CoffeeShopLocation> getAllLocations() throws CsvReaderException {
        final RestTemplate restTemplate = new RestTemplate();
        final String csvData = restTemplate.getForObject(CSV_URL, String.class);
        final List<CoffeeShopLocation> coffeeShopLocations = new ArrayList<>();
        return getCoffeeShopLocations(csvData, coffeeShopLocations);
    }

    private List<CoffeeShopLocation> getCoffeeShopLocations(String csvData, List<CoffeeShopLocation> coffeeShopLocations) throws CsvReaderException {
        try (CSVReader reader = new CSVReader(new StringReader(csvData))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length != 3) {
                    final String errorMessage = "Skip malformed line " + String.join(",", line);
                    logger.error(errorMessage);
                    continue;
                }
                extractCoffeeShopLocations(coffeeShopLocations, line);
            }
        } catch (IOException | CsvValidationException e) {
            final String errorMessage = "Something went wrong when reading the CSV file";
            logger.error(errorMessage, e);
            throw new CsvReaderException(errorMessage, e);
        }

        return coffeeShopLocations;
    }

    private void extractCoffeeShopLocations(List<CoffeeShopLocation> coffeeShopLocations, String[] line) {
        try {
            final String name = line[0].trim();
            final double x = Double.parseDouble(line[1].trim());
            final double y = Double.parseDouble(line[2].trim());
            coffeeShopLocations.add(new CoffeeShopLocation(name, x, y));
        } catch (NumberFormatException e) {
            final String errorMessage = "Skip malformed line, the columns are not properly written (invalid coordinates) " + String.join(",", line);
            logger.error(errorMessage);
        }
    }
}
