package com.emanuel.coffee.addicts.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CsvDataFetcherService {
    private static final Logger logger = LoggerFactory.getLogger(CsvDataFetcherService.class);
    private final RestTemplate restTemplate;
    private final String csvUrl;

    public CsvDataFetcherService(RestTemplate restTemplate,
                                 @Value("${csv.url}") String csvUrl) {
        this.restTemplate = restTemplate;
        this.csvUrl = csvUrl;
    }

    public String getCsvData() {
        return restTemplate.getForObject(csvUrl, String.class);
    }
}
