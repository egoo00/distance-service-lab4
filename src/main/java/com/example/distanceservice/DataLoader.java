package com.example.distanceservice;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.entity.Country;
import com.example.distanceservice.entity.Tourist;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.repository.CountryRepository;
import com.example.distanceservice.repository.TouristRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final TouristRepository touristRepository;

    public DataLoader(CityRepository cityRepository, CountryRepository countryRepository, TouristRepository touristRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.touristRepository = touristRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Country russia = new Country(null, "Russia", null);
        countryRepository.save(russia);

        Country uk = new Country(null, "United Kingdom", null);
        countryRepository.save(uk);

        City moscow = new City("Moscow", 55.7558, 37.6173, russia, null);
        cityRepository.save(moscow);

        City london = new City("London", 51.5074, -0.1278, uk, null);
        cityRepository.save(london);

        Tourist ivan = new Tourist(null, "Ivan", Arrays.asList(moscow));
        touristRepository.save(ivan);

        Tourist john = new Tourist(null, "John", Arrays.asList(london));
        touristRepository.save(john);

        Tourist alice = new Tourist(null, "Alice", Arrays.asList(moscow, london));
        touristRepository.save(alice);
    }
}