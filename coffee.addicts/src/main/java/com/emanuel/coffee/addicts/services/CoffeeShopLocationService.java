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
import java.util.*;

@Service
public class CoffeeShopLocationService {
    private static final String CSV_URL = "https://raw.githubusercontent.com/Agilefreaks/test_oop/master/coffee_shops.csv";
    private static final Logger logger = LoggerFactory.getLogger(CoffeeShopLocationService.class);
    public Set<CoffeeShopLocation> getAllLocations() {
        final RestTemplate restTemplate = new RestTemplate();
        final String csvData = restTemplate.getForObject(CSV_URL, String.class);
        final Set<CoffeeShopLocation> coffeeShopLocations = new HashSet<>();
        return parseCvsLocations(csvData, coffeeShopLocations);
    }

    public Set<CoffeeShopLocation> parseCvsLocations(String csvData, Set<CoffeeShopLocation> coffeeShopLocations) {
        try (CSVReader reader = new CSVReader(new StringReader(csvData))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length != 3) {
                    logger.warn("Skipping malformed or header line: {}", String.join(",", line));
                    continue;
                }
                addLocation(coffeeShopLocations, line);
            }
        } catch (IOException | CsvValidationException e) {
            final String errorMessage = "Something went wrong when reading the CSV file";
            logger.error(errorMessage, e);
        }
        return coffeeShopLocations;
    }

    private void addLocation(Set<CoffeeShopLocation> coffeeShopLocations, String[] line) {
        try {
            final String name = line[0].trim();
            final double x = Double.parseDouble(line[1].trim());
            final double y = Double.parseDouble(line[2].trim());
            coffeeShopLocations.add(new CoffeeShopLocation(name, x, y));
        } catch (NumberFormatException e) {
            logger.warn("Skipping line with invalid coordinates: {}", String.join(",", line));
        } catch (Exception e) {
            logger.error("Unexpected error while processing line: {}", String.join(",", line), e);
        }
    }
}
