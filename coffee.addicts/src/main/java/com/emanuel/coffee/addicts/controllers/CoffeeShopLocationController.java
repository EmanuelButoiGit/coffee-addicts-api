package com.emanuel.coffee.addicts.controllers;

import com.emanuel.coffee.addicts.dtos.CoffeeShopLocationDto;
import com.emanuel.coffee.addicts.services.CoffeeShopLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class CoffeeShopLocationController {
    @Autowired
    CoffeeShopLocationService coffeeShopLocationService;

    @QueryMapping
    public List<CoffeeShopLocationDto> getAllFakeLocations(){
        return Collections.singletonList(new CoffeeShopLocationDto("Cafea Buna", 12, 122));
    }

    @QueryMapping
    public Set<CoffeeShopLocationDto> getAllLocations() {
        return coffeeShopLocationService.getAllLocations();
    }

    @QueryMapping
    public List<CoffeeShopLocationDto> getLocationsBasedOnCoordinates(@Argument double x, @Argument double y) {
        return coffeeShopLocationService.getLocationsBasedOnCoordinates(x, y);
    }
}
