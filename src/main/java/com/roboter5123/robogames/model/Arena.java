package com.roboter5123.robogames.model;


public class Arena {

    private String world;
    private Location lobbySpawn;
    private Location pos1;
    private Location pos2;


    public String getWorld() {
        return world;
    }

    public void setWorldName(String world) {
        this.world = world;
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

    public void setWorld(String world) {
        this.world = world;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    @Override
    public String toString() {
        // @formatter:off
        return "Arena{" +
                "world='" + world + '\'' +
                ", lobbySpawn=" + lobbySpawn +
                ", pos1=" + pos1 +
                ", pos2=" + pos2 +
                '}';
        // @formatter:on
    }
}
