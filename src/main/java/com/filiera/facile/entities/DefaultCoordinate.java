package com.filiera.facile.entities;

import com.filiera.facile.model.interfaces.Coordinate;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "coordinate")
public class DefaultCoordinate implements Coordinate {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    public DefaultCoordinate(Float latitude, Float longitude) {
        this.id = UUID.randomUUID();
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