package com.example.distanceservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cities")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    private String name;
    private double latitude;
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonBackReference
    private Country country;

    @ManyToMany(mappedBy = "visitedCities")
    @JsonBackReference
    private List<Tourist> tourists;
}