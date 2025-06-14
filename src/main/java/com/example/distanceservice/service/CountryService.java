package com.example.distanceservice.service;

import com.example.distanceservice.cache.SimpleCache;
import com.example.distanceservice.entity.Country;
import com.example.distanceservice.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final SimpleCache cache;

    public CountryService(CountryRepository countryRepository, SimpleCache cache) {
        this.countryRepository = countryRepository;
        this.cache = cache;
    }

    public List<Country> getAllCountries() {
        List<Country> cachedCountries = (List<Country>) cache.get("countries_all");
        if (cachedCountries != null) {
            return cachedCountries;
        }
        List<Country> countries = countryRepository.findAll();
        cache.put("countries_all", countries);
        return countries;
    }

    public Optional<Country> getCountryById(Long id) {
        String cacheKey = "country_" + id;
        Country cachedCountry = (Country) cache.get(cacheKey);
        if (cachedCountry != null) {
            return Optional.of(cachedCountry);
        }
        Optional<Country> country = countryRepository.findById(id);
        country.ifPresent(c -> cache.put(cacheKey, c));
        return country;
    }

    public Country saveCountry(Country country) {
        Country savedCountry = countryRepository.save(country);
        cache.put("country_" + country.getId(), savedCountry);
        cache.clear("countries_all");
        return savedCountry;
    }

    public Country updateCountry(Long id, Country country) {
        if (!countryRepository.existsById(id)) {
            throw new RuntimeException("Country not found");
        }
        country.setId(id);
        Country updatedCountry = countryRepository.save(country);
        cache.put("country_" + id, updatedCountry);
        cache.clear("countries_all");
        return updatedCountry;
    }

    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
        cache.clear("country_" + id);
        cache.clear("countries_all");
    }
}