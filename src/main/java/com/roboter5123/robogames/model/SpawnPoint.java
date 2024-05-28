package com.roboter5123.robogames.model;

import java.util.Objects;

public class SpawnPoint {

    private String world;
    private Location location;

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SpawnPoint that = (SpawnPoint) object;
        return Objects.equals(world, that.world) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, location);
    }

    @Override
    public String toString() {
        // @formatter:off
        return "SpawnPoint{" +
                "world=" + world +
                "location=" + location +
                '}';
        // @formatter:on
    }
}
