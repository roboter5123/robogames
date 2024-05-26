package com.roboter5123.robogames.model;

public class Arena {

    private String world;
    private Coordinate pos1;
    private Coordinate pos2;


    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Coordinate getPos1() {
        return pos1;
    }

    public void setPos1(Coordinate pos1) {
        this.pos1 = pos1;
    }

    public Coordinate getPos2() {
        return pos2;
    }

    public void setPos2(Coordinate pos2) {
        this.pos2 = pos2;
    }

    @Override
    public String toString() {
        // @formatter:off
        return "Arena{" +
                "world='" + world + '\'' +
                ", pos1=" + pos1 +
                ", pos2=" + pos2 +
                '}';
        // @formatter:on
    }
}
