package com.example.distanceservice.service;

import com.example.distanceservice.cache.SimpleCache;
import com.example.distanceservice.entity.City;
import com.example.distanceservice.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private final CityRepository cityRepository;
    private final SimpleCache cache;

    public CityService(CityRepository cityRepository, SimpleCache cache) {
        this.cityRepository = cityRepository;
        this.cache = cache;
    }

    public List<City> getAllCities() {
        List<City> cachedCities = (List<City>) cache.get("cities_all");
        if (cachedCities != null) {
            return cachedCities;
        }
        List<City> cities = cityRepository.findAll();
        cache.put("cities_all", cities);
        return cities;
    }

    public Optional<City> getCityById(String id) {
        String cacheKey = "city_" + id;
        City cachedCity = (City) cache.get(cacheKey);
        if (cachedCity != null) {
            return Optional.of(cachedCity);
        }
        Optional<City> city = cityRepository.findById(id);
        city.ifPresent(c -> cache.put(cacheKey, c));
        return city;
    }

    public City saveCity(City city) {
        City savedCity = cityRepository.save(city);
        cache.put("city_" + city.getName(), savedCity);
        cache.clear("cities_all");
        return savedCity;
    }

    public City updateCity(String id, City city) {
        if (!cityRepository.existsById(id)) {
            throw new RuntimeException("City not found");
        }
        city.setName(id);
        City updatedCity = cityRepository.save(city);
        cache.put("city_" + id, updatedCity);
        cache.clear("cities_all");
        return updatedCity;
    }

    public void deleteCity(String id) {
        cityRepository.deleteById(id);
        cache.clear("city_" + id);
        cache.clear("cities_all");
    }
}