package design.model.memento;

import design.model.path.PathPoint;
import design.model.path.PathPointState;
import design.model.shelf.Shelf;
import design.model.Storage;
import design.model.cart.Cart;

import java.util.ArrayList;

public class CareTaker {
    public Storage storage;
    public ArrayList<ObjectCareTaker> cartCareTakers = new ArrayList<>();

    public CareTaker(Storage storage) {
        this.storage = storage;
    }

    public void saveState() {
        for (PathPoint point : this.storage.getPath().getPoints()) {
            point.getCareTaker().addState(point.saveStateToMemento());
        }

        for (Shelf shelf : this.storage.getAllShelfs()) {
            shelf.getCareTaker().addState(shelf.saveStateToMemento());
        }

        for (Cart cart : this.storage.getPath().getCarts()) {
            cart.getCareTaker().addState(cart.saveStateToMemento());
        }
    }

    public void setState(int index) {
        for (PathPoint point : this.storage.getPath().getPoints()) {
            point.setStateFromMemento(point.getCareTaker().getState(index));
        }

        for (Shelf shelf : this.storage.getAllShelfs()) {
            shelf.setStateFromMemento(shelf.getCareTaker().getState(index));
        }

        for (Cart cart : this.storage.getPath().getCarts()) {
            cart.setStateFromMemento(cart.getCareTaker().getState(index));
        }
    }
}
