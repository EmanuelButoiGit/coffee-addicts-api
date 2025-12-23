package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.dtos.CoffeeShopLocationDto;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

@Service
public class CsvParserService {
    private static final int NAME_INDEX = 0;
    private static final int X_INDEX = 1;
    private static final int Y_INDEX = 2;
    private static final Logger logger = LoggerFactory.getLogger(CsvParserService.class);
    public Set<CoffeeShopLocationDto> parseCsvLocations(String csvData, Set<CoffeeShopLocationDto> coffeeShopLocations) {
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
            logger.error("CSV reading or parsing failed", e);
        } catch (Exception e) {
            logger.error("Unexpected error while parsing CSV", e);
        }
        return coffeeShopLocations;
    }

    private void addLocation(Set<CoffeeShopLocationDto> coffeeShopLocations, String[] line) {
        try {
            final String name = line[NAME_INDEX].trim();
            final double x = Double.parseDouble(line[X_INDEX].trim());
            final double y = Double.parseDouble(line[Y_INDEX].trim());
            coffeeShopLocations.add(new CoffeeShopLocationDto(name, x, y));
        } catch (NumberFormatException e) {
            logger.warn("Skipping line with invalid coordinates: {}", String.join(",", line));
        } catch (Exception e) {
            logger.error("Unexpected error while processing line: {}", String.join(",", line), e);
        }
    }
}
