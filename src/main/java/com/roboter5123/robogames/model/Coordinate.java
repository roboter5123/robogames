package com.roboter5123.robogames.model;

import java.util.Objects;

public class Coordinate {

    private Double xCoordinate;
    private Double yCoordinate;
    private Double zCoordinate;

    public Double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Double getzCoordinate() {
        return zCoordinate;
    }

    public void setzCoordinate(Double zCoordinate) {
        this.zCoordinate = zCoordinate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coordinate that = (Coordinate) object;
        return Objects.equals(xCoordinate, that.xCoordinate) && Objects.equals(yCoordinate, that.yCoordinate) && Objects.equals(zCoordinate, that.zCoordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoordinate, yCoordinate, zCoordinate);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", zCoordinate=" + zCoordinate +
                '}';
    }
}
