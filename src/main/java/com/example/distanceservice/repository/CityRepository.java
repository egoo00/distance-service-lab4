package com.example.distanceservice.repository;

import com.example.distanceservice.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, String> {
    Optional<City> findByName(String name);

    @Query("SELECT c FROM City c WHERE c.country.name = ?1")
    List<City> findCitiesByCountryName(String countryName);

    @Query(value = "SELECT * FROM cities c JOIN countries co ON c.country_id = co.id WHERE co.name = ?1", nativeQuery = true)
    List<City> findCitiesByCountryNameNative(String countryName);
}