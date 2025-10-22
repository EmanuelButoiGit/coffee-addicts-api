package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CoffeeShopLocationServiceTest {
    private static final String CSV_URL = "https://raw.githubusercontent.com/Agilefreaks/test_oop/master/coffee_shops.csv";
    private RestTemplate restTemplateMock;
    private CoffeeShopLocationService coffeeShopLocationService;

    @BeforeEach
    void setUp() {
        restTemplateMock = Mockito.mock(RestTemplate.class);
        CsvDataFetcherService csvDataFetcherService = new CsvDataFetcherService(restTemplateMock);
        CsvParserService csvParserService = new CsvParserService();
        coffeeShopLocationService = new CoffeeShopLocationService(csvDataFetcherService, csvParserService);
    }

    private void mockCsvResponse(String fileName) throws IOException {
        String csvData = ReadCsvFileHelper.readCsvFile(fileName);
        when(restTemplateMock.getForObject(eq(CSV_URL), eq(String.class))).thenReturn(csvData);
    }

    private void assertLocation(CoffeeShopLocation expected, CoffeeShopLocation actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getX(), actual.getX());
        assertEquals(expected.getY(), actual.getY());
    }

    @Test
    void getLocationsBasedOnCoordinates_GitHubCsv() throws IOException {
        // Arrange
        mockCsvResponse("GitHubCsv.csv");
        CoffeeShopLocation expected1 = new CoffeeShopLocation("Starbucks Seattle2", 47.5869, -122.3368);
        CoffeeShopLocation expected2 = new CoffeeShopLocation("Starbucks Seattle", 47.5809, -122.3160);
        CoffeeShopLocation expected3 = new CoffeeShopLocation("Starbucks SF", 37.5209, -122.3340);

        // Act
        List<CoffeeShopLocation> result = coffeeShopLocationService.getLocationsBasedOnCoordinates(47.6, -122.4);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertLocation(expected1, result.get(0));
        assertLocation(expected2, result.get(1));
        assertLocation(expected3, result.get(2));
    }

    @Test
    void getLocationsBasedOnCoordinates_TwoEntriesCsv() throws IOException {
        // Arrange
        mockCsvResponse("TwoEntriesCsv.csv");
        CoffeeShopLocation expected1 = new CoffeeShopLocation("Starbucks Seattle2", 47.5869, -122.3368);
        CoffeeShopLocation expected2 = new CoffeeShopLocation("Starbucks Seattle", 47.5809, -122.3160);

        // Act
        List<CoffeeShopLocation> result = coffeeShopLocationService.getLocationsBasedOnCoordinates(47.6, -122.4);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertLocation(expected1, result.get(0));
        assertLocation(expected2, result.get(1));
    }
}
