package design.model;

public class PathPoint {
    private Integer posX;
    private Integer posY;
    private Boolean isBlocked;

    public PathPoint(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
        this.isBlocked = false;
    }

    public Integer getPosX() {
        return this.posX;
    }

    public Integer getPosY() {
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
}
