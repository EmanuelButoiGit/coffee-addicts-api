package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CoffeeShopLocationServiceTest {
    private CoffeeShopLocationService coffeeShopLocationService;
    @BeforeEach
    void setUp() {
        final RestTemplate restTemplateMock = mock(RestTemplate.class);
        coffeeShopLocationService = new CoffeeShopLocationService(restTemplateMock);
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
    void extractCoffeeShopLocations_GitHubCsv() throws IOException {
        final String csvData = readCsvFile("GitHubCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(6, result.size());
    }

    @Test
    void extractCoffeeShopLocations_SpacesTabsEndlineCsv() throws IOException {
        final String csvData = readCsvFile("SpacesTabsEndlineCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(4, result.size());
    }

    @Test
    void extractCoffeeShopLocations_SymbolsCsv() throws IOException {
        final String csvData = readCsvFile("SymbolsCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(10, result.size());
    }

    @Test
    void extractCoffeeShopLocations_EncryptedCsv() throws IOException {
        final String csvData = readCsvFile("EncryptedCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_HeaderCsv() throws IOException {
        final String csvData = readCsvFile("HeaderCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(6, result.size());
    }

    @Test
    void extractCoffeeShopLocations_DuplicatesCsv() throws IOException {
        final String csvData = readCsvFile("DuplicatesCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(7, result.size());
    }

    @Test
    void extractCoffeeShopLocations_OnlyHeaderCsv() throws IOException {
        final String csvData = readCsvFile("OnlyHeaderCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_EmptyCsv() throws IOException {
        final String csvData = readCsvFile("EmptyCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_NullCsv() throws IOException {
        final String csvData = readCsvFile("NullCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_LargeCsv() throws IOException {
        final String csvData = readCsvFile("LargeCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(10000, result.size());
    }

    @Test
    void extractCoffeeShopLocations_ExtremeValuesCsv() throws IOException {
        final String csvData = readCsvFile("ExtremeValuesCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(5, result.size());
    }

    @Test
    void extractCoffeeShopLocations_MaliciousCsv() throws IOException {
        final String csvData = readCsvFile("MaliciousCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void extractCoffeeShopLocations_MixedDelimitersCsv() throws IOException {
        final String csvData = readCsvFile("MixedDelimitersCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_ExtremeMixedDelimitersCsv() throws IOException {
        final String csvData = readCsvFile("ExtremeMixedDelimitersCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void extractCoffeeShopLocations_FourColumnsCsvCsv() throws IOException {
        final String csvData = readCsvFile("FourColumnsCsv.csv");
        final Set<CoffeeShopLocation> result = coffeeShopLocationService.parseCsvLocations(csvData, new HashSet<>());
        assertTrue(result.isEmpty());
    }
}
