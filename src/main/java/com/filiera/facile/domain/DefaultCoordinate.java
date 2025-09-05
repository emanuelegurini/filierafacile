package com.filiera.facile.domain;

import com.filiera.facile.model.interfaces.Coordinate;

import java.util.Objects;

public class DefaultCoordinate implements Coordinate {

    private Float latitude;
    private Float longitude;

    public DefaultCoordinate(Float latitude, Float longitude) {
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
