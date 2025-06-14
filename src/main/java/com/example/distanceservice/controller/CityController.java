package com.example.distanceservice.controller;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Operation(summary = "Get all cities", description = "Returns a list of all cities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @Operation(summary = "Get city by ID", description = "Returns a city by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved city"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable String id) {
        Optional<City> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new city", description = "Creates a new city and returns the created city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created city"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        City savedCity = cityService.saveCity(city);
        return ResponseEntity.status(201).body(savedCity);
    }

    @Operation(summary = "Update an existing city", description = "Updates a city by its name and returns the updated city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated city"),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable String id, @RequestBody City city) {
        try {
            City updatedCity = cityService.updateCity(id, city);
            return ResponseEntity.ok(updatedCity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a city", description = "Deletes a city by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted city"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable String id) {
        try {
            cityService.deleteCity(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}