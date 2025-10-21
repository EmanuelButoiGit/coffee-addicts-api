package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import com.emanuel.coffee.addicts.objects.Result;
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
    private final RestTemplate restTemplate;

    public CoffeeShopLocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Set<CoffeeShopLocation> getAllLocations() {
        final String csvData = restTemplate.getForObject(CSV_URL, String.class);
        final Set<CoffeeShopLocation> coffeeShopLocations = new HashSet<>();
        return parseCsvLocations(csvData, coffeeShopLocations);
    }

    public Set<CoffeeShopLocation> parseCsvLocations(String csvData, Set<CoffeeShopLocation> coffeeShopLocations) {
        if (csvData == null || csvData.isEmpty()) {
            logger.warn("CSV data is empty or null");
            return coffeeShopLocations;
        }
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

    public List<CoffeeShopLocation> getLocationsBasedOnCoordinates(double x, double y) {
        final Set<CoffeeShopLocation> locationSet = getAllLocations();
        if (locationSet.isEmpty()) {
            return Collections.emptyList();
        }
        final List<CoffeeShopLocation> locations = new ArrayList<>(locationSet);
        List<Result> resultMap = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            final CoffeeShopLocation location = locations.get(i);
            if (location == null) {
                logger.warn("Skipping null location at index {}", i);
                continue;
            }
            try {
                final double distance = calculateDistance(x, y, location.getX(), location.getY());
                resultMap.add(new Result(distance, i));
            } catch (NullPointerException | IllegalArgumentException e) {
                logger.warn("Skipping invalid location at index {}: {}", i, e.getMessage());
            } catch (Exception e) {
                logger.error("Something went wrong..", e);
            }
        }
        resultMap.sort(Comparator.comparingDouble(Result::distance));
        List<CoffeeShopLocation> nearestLocations = new ArrayList<>();
        for (int i = 0; i < Math.min(3, resultMap.size()); i++) {
            nearestLocations.add(locations.get(resultMap.get(i).position()));
        }
        return nearestLocations;
    }

    public double calculateDistance(double x, double y, double locationX, double locationY) {
        final double dx = locationX - x;
        final double dy = locationY - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
