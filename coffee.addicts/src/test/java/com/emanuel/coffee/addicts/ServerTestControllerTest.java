package com.emanuel.coffee.addicts;

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
        ServerTest expectedServerTest = new ServerTest();
        expectedServerTest.setStatus("The app is on!");
        expectedServerTest.setDate(LocalDateTime.now().toString());

        // Act
        ServerTest result = graphQlTester
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
