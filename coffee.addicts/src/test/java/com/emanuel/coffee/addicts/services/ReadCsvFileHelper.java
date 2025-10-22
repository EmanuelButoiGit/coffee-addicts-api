package com.emanuel.coffee.addicts.services;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

final public class ReadCsvFileHelper {
    private ReadCsvFileHelper(){}
    public static String readCsvFile(String fileName) throws IOException {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("cvs/" + fileName)) {
            assertNotNull(inputStream, "Missing test CSV file: " + fileName);
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
