package com.roboter5123.robogames.model;

import org.bukkit.Location;

public class Arena {

    private String worldName;
    private Location pos1;
    private Location pos2;


    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String world) {
        this.worldName = world;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    @Override
    public String toString() {
        // @formatter:off
        return "Arena{" +
                "world='" + worldName + '\'' +
                ", pos1=" + pos1 +
                ", pos2=" + pos2 +
                '}';
        // @formatter:on
    }
}
