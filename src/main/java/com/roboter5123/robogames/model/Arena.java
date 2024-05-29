package com.roboter5123.robogames.model;

import org.bukkit.Location;

public class Arena {

    private String name;
    private String worldName;
    private String lobbyName;
    private Location pos1;
    private Location pos2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String world) {
        this.worldName = world;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
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
                "name='" + name + '\'' +
                "worldName='" + worldName + '\'' +
                "lobbyName='" + lobbyName + '\'' +
                ", pos1=" + pos1 +
                ", pos2=" + pos2 +
                '}';
        // @formatter:on
    }
}
