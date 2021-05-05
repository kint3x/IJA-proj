package design.model.path;

import design.model.memento.Memento;
import design.model.memento.ObjectCareTaker;
import design.model.memento.Originator;
import design.model.memento.State;

import java.util.Objects;

public class PathPoint implements Originator {
    private ObjectCareTaker pathCareTaker = new ObjectCareTaker();
    private int posX;
    private int posY;
    private Boolean isBlocked;
    private PathPointState state;

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
    public State getState() {
        return this.state;
    }

    @Override
    public void setState(State state) {
        // TODO listener

        this.state = (PathPointState) state;
    }

    @Override
    public Memento saveStateToMemento() {
        return new Memento(this.getState());
    }

    @Override
    public void setStateFromMemento(Memento memento) {
        this.setState(memento.getState());
    }

    @Override
    public ObjectCareTaker getCareTaker() {
        return this.pathCareTaker;
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
