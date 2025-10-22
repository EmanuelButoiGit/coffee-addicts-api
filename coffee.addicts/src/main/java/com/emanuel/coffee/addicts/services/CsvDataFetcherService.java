package com.emanuel.coffee.addicts.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CsvDataFetcherService {
    private static final String CSV_URL = "https://raw.githubusercontent.com/Agilefreaks/test_oop/master/coffee_shops.csv";
    private static final Logger logger = LoggerFactory.getLogger(CsvDataFetcherService.class);
    private final RestTemplate restTemplate;

    public CsvDataFetcherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCsvData() {
        try {
            return restTemplate.getForObject(CSV_URL, String.class);
        } catch (Exception e) {
            logger.error("Failed to fetch CSV data from URL: {}", CSV_URL, e);
            return "";
        }
    }
}
