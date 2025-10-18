package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeShopLocationServiceTest {
    private CoffeeShopLocationService coffeeShopLocationService;
    @BeforeEach
    void setUp() {
        coffeeShopLocationService = new CoffeeShopLocationService();
    }

    private String readCsvFile(String fileName) throws IOException {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("cvs/" + fileName)) {
            assertNotNull(inputStream);
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }

    @Test
    void extractCoffeeShopLocations_GitHubCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("GitHubCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(6, result.size());
    }

    @Test
    void extractCoffeeShopLocations_SpacesTabsEndlineCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("SpacesTabsEndlineCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(4, result.size());
    }

    @Test
    void extractCoffeeShopLocations_SymbolsCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("SymbolsCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(6, result.size());
    }

    @Test
    void extractCoffeeShopLocations_EncryptedCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("EncryptedCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_HeaderCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("HeaderCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(6, result.size());
    }

    @Test
    void extractCoffeeShopLocations_DuplicatesCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("DuplicatesCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(7, result.size());
    }

    @Test
    void extractCoffeeShopLocations_OnlyHeaderCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("OnlyHeaderCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_EmptyCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("EmptyCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_NullCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("NullCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_LargeCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("LargeCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(10000, result.size());
    }

    @Test
    void extractCoffeeShopLocations_ExtremeValuesCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("ExtremeValuesCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(5, result.size());
    }

    @Test
    void extractCoffeeShopLocations_MaliciousCsv() throws IOException, CsvReaderException {
        String csvData = readCsvFile("MaliciousCsv.csv");
        Set<CoffeeShopLocation> result = coffeeShopLocationService.getCoffeeShopLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }
}
