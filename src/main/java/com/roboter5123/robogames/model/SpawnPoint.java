package com.roboter5123.robogames.model;

import java.util.Objects;

public class SpawnPoint {

    private String world;
    private Coordinate coordinate;

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SpawnPoint that = (SpawnPoint) object;
        return Objects.equals(world, that.world) && Objects.equals(coordinate, that.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, coordinate);
    }

    @Override
    public String toString() {
        // @formatter:off
        return "SpawnPoint{" +
                "world=" + world +
                "coordinate=" + coordinate +
                '}';
        // @formatter:on
    }
}
