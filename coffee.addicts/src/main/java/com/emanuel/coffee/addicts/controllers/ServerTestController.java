package com.emanuel.coffee.addicts.controllers;

import com.emanuel.coffee.addicts.dtos.ServerTestDto;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ServerTestController {
    @QueryMapping
    public ServerTestDto testServer(){
        return new ServerTestDto("The app is on!");
    }
}
