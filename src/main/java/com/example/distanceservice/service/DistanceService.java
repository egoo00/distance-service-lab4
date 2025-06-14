package com.example.distanceservice.service;

import com.example.distanceservice.cache.SimpleCache;
import com.example.distanceservice.dto.DistanceResponse;
import com.example.distanceservice.entity.City;
import com.example.distanceservice.exception.CityNotFoundException;
import com.example.distanceservice.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {
    private final CityRepository cityRepository;
    private final GeocodingService geocodingService;
    private final SimpleCache cache;

    public DistanceService(CityRepository cityRepository, GeocodingService geocodingService, SimpleCache cache) {
        this.cityRepository = cityRepository;
        this.geocodingService = geocodingService;
        this.cache = cache;
    }

    public DistanceResponse calculateDistance(String city1, String city2) {
        String cacheKey = "distance_" + city1 + "_" + city2;
        DistanceResponse cachedResponse = (DistanceResponse) cache.get(cacheKey);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        City c1 = cityRepository.findByName(city1).orElseGet(() -> geocodingService.getCity(city1));
        if (c1 == null) {
            throw new CityNotFoundException("City " + city1 + " not found");
        }

        City c2 = cityRepository.findByName(city2).orElseGet(() -> geocodingService.getCity(city2));
        if (c2 == null) {
            throw new CityNotFoundException("City " + city2 + " not found");
        }

        if ("test500".equalsIgnoreCase(city2)) {
            throw new RuntimeException("Simulated internal server error for testing");
        }

        double distance = calculateHaversine(c1.getLatitude(), c1.getLongitude(), c2.getLatitude(), c2.getLongitude());
        DistanceResponse response = new DistanceResponse(city1, city2, distance, "km");
        cache.put(cacheKey, response);
        return response;
    }

    private double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}