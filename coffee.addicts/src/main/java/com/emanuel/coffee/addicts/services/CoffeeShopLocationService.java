package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.dtos.CoffeeShopLocationDto;
import com.emanuel.coffee.addicts.dtos.ResultDto;
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

    public Set<CoffeeShopLocationDto> getAllLocations() {
        final String csvData = csvDataFetcherService.getCsvData();
        final Set<CoffeeShopLocationDto> coffeeShopLocations = new HashSet<>();
        return csvParserService.parseCsvLocations(csvData, coffeeShopLocations);
    }

    public List<CoffeeShopLocationDto> getLocationsBasedOnCoordinates(double x, double y) {
        validateCoordinate(x, y);
        final Set<CoffeeShopLocationDto> locationSet = getAllLocations();
        if (locationSet.isEmpty()) {
            return Collections.emptyList();
        }
        final List<CoffeeShopLocationDto> locations = new ArrayList<>(locationSet);
        List<ResultDto> resultMap = new ArrayList<>();
        computeResultMap(x, y, locations, resultMap);
        return getNearestLocations(locations, resultMap);
    }

    private void computeResultMap(double x, double y, List<CoffeeShopLocationDto> locations, List<ResultDto> resultMap) {
        for (int i = 0; i < locations.size(); i++) {
            final CoffeeShopLocationDto location = locations.get(i);
            if (location == null) {
                logger.warn("Skipping null location at index {}", i);
                continue;
            }
            try {
                validateCoordinate(location.getX(), location.getY());
                final double distance = calculateDistance(x, y, location.getX(), location.getY());
                resultMap.add(new ResultDto(distance, i));
            } catch (NullPointerException | IllegalArgumentException e) {
                logger.warn("Skipping invalid location at index {}: {}", i, e.getMessage());
            } catch (Exception e) {
                logger.error("Error calculating distance for location at index {}: {}", i, location, e);
            }
        }
    }

    private List<CoffeeShopLocationDto> getNearestLocations(List<CoffeeShopLocationDto> locations, List<ResultDto> resultMap) {
        resultMap.sort(Comparator.comparingDouble(ResultDto::distance));
        List<CoffeeShopLocationDto> nearestLocations = new ArrayList<>();
        for (int i = 0; i < Math.min(3, resultMap.size()); i++) {
            nearestLocations.add(locations.get(resultMap.get(i).position()));
        }
        return nearestLocations;
    }

    private void validateCoordinate(double x, double y) {
        final boolean isCoordinateNotANumber = Double.isNaN(x) || Double.isNaN(y);
        final boolean isCoordinateInfinite = Double.isInfinite(x) || Double.isInfinite(y);
        final boolean isCoordinateBiggerThanMaxDouble = Math.abs(x) > Double.MAX_VALUE || Math.abs(y) > Double.MAX_VALUE;
        if (isCoordinateNotANumber
                || isCoordinateInfinite
                || isCoordinateBiggerThanMaxDouble) {
            throw new IllegalArgumentException("Invalid coordinates: {x = + " + x + ", y = " + y + "}");
        }
    }

    public double calculateDistance(double x, double y, double locationX, double locationY) {
        final double dx = locationX - x;
        final double dy = locationY - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
