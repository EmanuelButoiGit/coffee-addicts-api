package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        mockCsvResponse("GitHubCsv.csv");
        CoffeeShopLocation expected1 = new CoffeeShopLocation("Starbucks Seattle2", 47.5869, -122.3368);
        CoffeeShopLocation expected2 = new CoffeeShopLocation("Starbucks Seattle", 47.5809, -122.3160);
        CoffeeShopLocation expected3 = new CoffeeShopLocation("Starbucks SF", 37.5209, -122.3340);

        List<CoffeeShopLocation> result = coffeeShopLocationService.getLocationsBasedOnCoordinates(47.6, -122.4);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertLocation(expected1, result.get(0));
        assertLocation(expected2, result.get(1));
        assertLocation(expected3, result.get(2));
    }

    @Test
    void getLocationsBasedOnCoordinates_TwoEntriesCsv() throws IOException {
        mockCsvResponse("TwoEntriesCsv.csv");
        CoffeeShopLocation expected1 = new CoffeeShopLocation("Starbucks Seattle2", 47.5869, -122.3368);
        CoffeeShopLocation expected2 = new CoffeeShopLocation("Starbucks Seattle", 47.5809, -122.3160);

        List<CoffeeShopLocation> result = coffeeShopLocationService.getLocationsBasedOnCoordinates(47.6, -122.4);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertLocation(expected1, result.get(0));
        assertLocation(expected2, result.get(1));
    }

    @Test
    void getLocationsBasedOnCoordinates_WhenRemoteCsvUnavailable() {
        when(restTemplateMock.getForObject(eq(CSV_URL), eq(String.class))).thenReturn(null);

        List<CoffeeShopLocation> result = coffeeShopLocationService.getLocationsBasedOnCoordinates(47.6, -122.4);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLocationsBasedOnCoordinates_WhenHttpError() {
        when(restTemplateMock.getForObject(eq(CSV_URL), eq(String.class)))
                .thenThrow(new RuntimeException("HTTP 404"));

        List<CoffeeShopLocation> result = coffeeShopLocationService.getLocationsBasedOnCoordinates(47.6, -122.4);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLocationsBasedOnCoordinates_BadInput() throws IOException {
        mockCsvResponse("GitHubCsv.csv");
        final double[][] faultyCoordinates = {
                {Double.NaN, -122.4},
                {47.6, Double.NaN},
                {Double.POSITIVE_INFINITY, 10.0},
                {Double.NEGATIVE_INFINITY, -10.0},
        };

        for (double[] coord : faultyCoordinates) {
            final double x = coord[0];
            final double y = coord[1];
            assertThrows(IllegalArgumentException.class,
                    () -> coffeeShopLocationService.getLocationsBasedOnCoordinates(x, y),
                    "Expected IllegalArgumentException for invalid coordinates: x=" + x + ", y=" + y);

        }
    }
    @Test
    void getLocationsBasedOnCoordinates_GoodInput() throws IOException {
        mockCsvResponse("GitHubCsv.csv");
        final double[][] nonFaultyCoordinates = {
                {0.0, 0.0},
                {9999999.0, -9999999.0},
                {-9999999.0, 9999999.0},
                {Double.MAX_VALUE, Double.MIN_VALUE},
                {Double.MIN_NORMAL, -Double.MIN_NORMAL}
        };

        for (double[] coord : nonFaultyCoordinates) {
            final double x = coord[0];
            final double y = coord[1];
            final List<CoffeeShopLocation> result = coffeeShopLocationService.getLocationsBasedOnCoordinates(x, y);
            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    private record CsvTestCase(String fileName, boolean expectEmpty) {}

    private Stream<CsvTestCase> csvTestCases() {
        return Stream.of(
                new CsvTestCase("GitHubCsv.csv", false),
                new CsvTestCase("SpacesTabsEndlineCsv.csv", false),
                new CsvTestCase("SymbolsCsv.csv", false),
                new CsvTestCase("EncryptedCsv.csv", true),
                new CsvTestCase("HeaderCsv.csv", false),
                new CsvTestCase("DuplicatesCsv.csv", false),
                new CsvTestCase("OnlyHeaderCsv.csv", true),
                new CsvTestCase("EmptyCsv.csv", true),
                new CsvTestCase("NullCsv.csv", true),
                new CsvTestCase("LargeCsv.csv", false),
                new CsvTestCase("ExtremeValuesCsv.csv", false),
                new CsvTestCase("MaliciousCsv.csv", false),
                new CsvTestCase("MixedDelimitersCsv.csv", true),
                new CsvTestCase("ExtremeMixedDelimitersCsv.csv", true),
                new CsvTestCase("FourColumnsCsv.csv", true)
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("csvTestCases")
    void getLocationsBasedOnCoordinates_AllCsvs(CsvTestCase csvTestCase) throws IOException {
        mockCsvResponse(csvTestCase.fileName());

        final List<CoffeeShopLocation> result = coffeeShopLocationService.getLocationsBasedOnCoordinates(47.6, -122.4);

        assertNotNull(result, "Service returned null for CSV: " + csvTestCase.fileName());

        if (csvTestCase.expectEmpty()) {
            assertTrue(result.isEmpty(), "Expected empty result for CSV: " + csvTestCase.fileName());
        } else {
            assertFalse(result.isEmpty(), "Expected non-empty result for CSV: " + csvTestCase.fileName());
        }
    }
}
