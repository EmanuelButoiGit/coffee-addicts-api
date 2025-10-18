package com.emanuel.coffee.addicts.controllers;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import com.emanuel.coffee.addicts.services.CoffeeShopLocationService;
import com.emanuel.coffee.addicts.services.CsvReaderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Set;

@GraphQlTest(CoffeeShopLocationController.class)
class CoffeeShopLocationControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;
    @MockBean
    private CoffeeShopLocationService coffeeShopLocationService;

    @Test
    void getAllLocations() throws CsvReaderException {
        // Arrange
        final CoffeeShopLocation expectedLocation = new CoffeeShopLocation();
        expectedLocation.setName("Ted's Coffee");
        expectedLocation.setX(1.0);
        expectedLocation.setY(2.0);
        Mockito.when(coffeeShopLocationService.getAllLocations())
                .thenReturn(Set.of(expectedLocation));

        // Act
        final List<CoffeeShopLocation> result = graphQlTester
                .documentName("getAllLocations")
                .execute()
                .path("getAllLocations")
                .entityList(CoffeeShopLocation.class)
                .get();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(expectedLocation.getName(), result.get(0).getName());
        Assertions.assertEquals(expectedLocation.getX(), result.get(0).getX());
        Assertions.assertEquals(expectedLocation.getY(), result.get(0).getY());
    }
}
