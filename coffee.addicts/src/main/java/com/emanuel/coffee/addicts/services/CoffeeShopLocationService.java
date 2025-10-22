package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import com.emanuel.coffee.addicts.objects.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoffeeShopLocationService {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeShopLocationService.class);
    private final CsvDataFetcherService csvDataFetcherService;
    private final CsvParserService csvParserService;

    public CoffeeShopLocationService(CsvDataFetcherService csvDataFetcherService, CsvParserService csvParserService) {
        this.csvDataFetcherService = csvDataFetcherService;
        this.csvParserService = csvParserService;
    }

    public Set<CoffeeShopLocation> getAllLocations() {
        final String csvData = csvDataFetcherService.getCsvData();
        final Set<CoffeeShopLocation> coffeeShopLocations = new HashSet<>();
        return csvParserService.parseCsvLocations(csvData, coffeeShopLocations);
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
