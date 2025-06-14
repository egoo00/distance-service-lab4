package com.example.distanceservice.service;

import com.example.distanceservice.cache.SimpleCache;
import com.example.distanceservice.entity.Tourist;
import com.example.distanceservice.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TouristService {
    private final TouristRepository touristRepository;
    private final SimpleCache cache;

    public TouristService(TouristRepository touristRepository, SimpleCache cache) {
        this.touristRepository = touristRepository;
        this.cache = cache;
    }

    public List<Tourist> getAllTourists() {
        List<Tourist> cachedTourists = (List<Tourist>) cache.get("tourists_all");
        if (cachedTourists != null) {
            return cachedTourists;
        }
        List<Tourist> tourists = touristRepository.findAll();
        cache.put("tourists_all", tourists);
        return tourists;
    }

    public Optional<Tourist> getTouristById(Long id) {
        String cacheKey = "tourist_" + id;
        Tourist cachedTourist = (Tourist) cache.get(cacheKey);
        if (cachedTourist != null) {
            return Optional.of(cachedTourist);
        }
        Optional<Tourist> tourist = touristRepository.findById(id);
        tourist.ifPresent(t -> cache.put(cacheKey, t));
        return tourist;
    }

    public Tourist saveTourist(Tourist tourist) {
        Tourist savedTourist = touristRepository.save(tourist);
        cache.put("tourist_" + tourist.getId(), savedTourist);
        cache.clear("tourists_all");
        return savedTourist;
    }

    public Tourist updateTourist(Long id, Tourist tourist) {
        if (!touristRepository.existsById(id)) {
            throw new RuntimeException("Tourist not found");
        }
        tourist.setId(id);
        Tourist updatedTourist = touristRepository.save(tourist);
        cache.put("tourist_" + id, updatedTourist);
        cache.clear("tourists_all");
        return updatedTourist;
    }

    public void deleteTourist(Long id) {
        touristRepository.deleteById(id);
        cache.clear("tourist_" + id);
        cache.clear("tourists_all");
    }
}