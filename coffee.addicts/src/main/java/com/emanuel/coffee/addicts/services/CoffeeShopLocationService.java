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
        validateCoordinate(x, y);
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
                validateCoordinate(location.getX(), location.getY());
                final double distance = calculateDistance(x, y, location.getX(), location.getY());
                resultMap.add(new Result(distance, i));
            } catch (NullPointerException | IllegalArgumentException e) {
                logger.warn("Skipping invalid location at index {}: {}", i, e.getMessage());
            } catch (Exception e) {
                logger.error("Error calculating distance for location at index {}: {}", i, location, e);
            }
        }
        resultMap.sort(Comparator.comparingDouble(Result::distance));
        List<CoffeeShopLocation> nearestLocations = new ArrayList<>();
        for (int i = 0; i < Math.min(3, resultMap.size()); i++) {
            nearestLocations.add(locations.get(resultMap.get(i).position()));
        }
        return nearestLocations;
    }

    private void validateCoordinate(double x, double y) {
        if (Double.isNaN(x) ||Double.isNaN(y)
                || Double.isInfinite(x) || Double.isInfinite(y)
                || Math.abs(x) > Double.MAX_VALUE || Math.abs(y) > Double.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid coordinates: {x = + " + x + ", y = " + y + "}");
        }
    }

    public double calculateDistance(double x, double y, double locationX, double locationY) {
        final double dx = locationX - x;
        final double dy = locationY - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
