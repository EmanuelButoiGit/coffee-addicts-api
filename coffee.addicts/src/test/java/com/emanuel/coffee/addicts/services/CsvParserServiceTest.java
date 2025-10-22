package com.emanuel.coffee.addicts.services;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvParserServiceTest {
    private final CsvParserService csvParserService = new CsvParserService();
    private record CsvTestCase(String fileName, boolean expectEmpty, int expectedSize) {}

    private Stream<CsvTestCase> csvTestCases() {
        return Stream.of(
                new CsvTestCase("GitHubCsv.csv", false, 6),
                new CsvTestCase("SpacesTabsEndlineCsv.csv", false, 4),
                new CsvTestCase("SymbolsCsv.csv", false, 10),
                new CsvTestCase("EncryptedCsv.csv", true, 0),
                new CsvTestCase("HeaderCsv.csv", false, 6),
                new CsvTestCase("DuplicatesCsv.csv", false, 7),
                new CsvTestCase("OnlyHeaderCsv.csv", true, 0),
                new CsvTestCase("EmptyCsv.csv", true, 0),
                new CsvTestCase("NullCsv.csv", true, 0),
                new CsvTestCase("LargeCsv.csv", false, 10000),
                new CsvTestCase("ExtremeValuesCsv.csv", false, 5),
                new CsvTestCase("MaliciousCsv.csv", false, 2),
                new CsvTestCase("MixedDelimitersCsv.csv", true, 0),
                new CsvTestCase("ExtremeMixedDelimitersCsv.csv", true, 0),
                new CsvTestCase("FourColumnsCsv.csv", true, 0)
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("csvTestCases")
    void extractCoffeeShopLocationsParameterized(CsvTestCase csvTestCase) throws IOException {
        final String csvData = ReadCsvFileHelper.readCsvFile(csvTestCase.fileName());
        final Set<CoffeeShopLocation> result = csvParserService.parseCsvLocations(csvData, new HashSet<>());

        if (csvTestCase.expectEmpty()) {
            assertTrue(result.isEmpty(), "Expected empty result for: " + csvTestCase.fileName());
        } else {
            assertFalse(result.isEmpty(), "Expected non-empty result for: " + csvTestCase.fileName());
            assertEquals(csvTestCase.expectedSize(), result.size(),
                    "Unexpected result size for: " + csvTestCase.fileName());
        }
    }
}
