package design.model;

import java.util.Objects;

public class PathPoint {
    private int posX;
    private int posY;
    private Boolean isBlocked;

    public PathPoint(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.isBlocked = false;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public void switchBlocked() {
        this.isBlocked = !this.isBlocked();
    }

    public Boolean isBlocked() {
        return this.isBlocked;
    }

    public void printPoint() {
        System.out.format("[%d, %d]\n", this.getPosX(), this.getPosY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathPoint point = (PathPoint) o;
        return getPosX() == point.getPosX() && getPosY() == point.getPosY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosX(), getPosY());
    }
}
