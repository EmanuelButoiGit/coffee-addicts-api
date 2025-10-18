package com.emanuel.coffee.addicts.controllers;

import com.emanuel.coffee.addicts.objects.CoffeeShopLocation;
import com.emanuel.coffee.addicts.objects.ServerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.time.LocalDateTime;

@GraphQlTest(ServerTestController.class)
class ServerTestControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void testServerApp(){
        //Assume
        final ServerTest expectedServerTest = new ServerTest();
        expectedServerTest.setStatus("The app is on!");
        expectedServerTest.setDate(LocalDateTime.now().toString());

        // Act
        final ServerTest result = graphQlTester
                .documentName("testServer")
                .execute()
                .path("testServer")
                .entity(ServerTest.class)
                .get();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedServerTest.getStatus(), result.getStatus());
        Assertions.assertEquals(
                LocalDateTime.parse(expectedServerTest.getDate()).toLocalDate(),
                LocalDateTime.parse(result.getDate()).toLocalDate()
        );
    }
}
