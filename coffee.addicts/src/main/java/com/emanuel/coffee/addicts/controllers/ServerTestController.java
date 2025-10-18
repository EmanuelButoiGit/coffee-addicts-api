package com.emanuel.coffee.addicts.controllers;

import com.emanuel.coffee.addicts.objects.ServerTest;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ServerTestController {
    @QueryMapping
    public ServerTest testServer(){
        return new ServerTest("The app is on!");
    }
}
