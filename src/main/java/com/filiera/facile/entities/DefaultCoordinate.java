package com.filiera.facile.entities;

import com.filiera.facile.model.interfaces.Coordinate;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "coordinate")
public class DefaultCoordinate implements Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    protected DefaultCoordinate() {}

    public DefaultCoordinate(Float latitude, Float longitude) {
        this.id = null;
        this.latitude = Objects.requireNonNull(latitude,"La latitudine non può essere lasciata vuota");
        this.longitude = Objects.requireNonNull(longitude,"La longitudine non può essere lasciata vuota ");
    }

    @Override
    public Float getLat() {
        return this.latitude;
    }

    @Override
    public Float getLng() {
        return this.longitude;
    }

    @Override
    public void setLat(Float lat) {
        this.latitude = Objects.requireNonNull(lat,"La latitudine non può essere lasciata vuota");
    }

    @Override
    public void setLng(Float lng) {
        this.longitude = Objects.requireNonNull(lng,"La longitudine non può essere lasciata vuota ");
    }
}