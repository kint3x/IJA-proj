package design.model.memento;

import design.model.path.PathPoint;
import design.model.shelf.Shelf;
import design.model.Storage;
import design.model.cart.Cart;

/**
 * Trieda spravujúca ukladanie a načítavanie stavov objektov.
 */
public class CareTaker {
    public Storage storage;

    /**
     * Konštruktor.
     * @param storage objekt skladu, ktorý spravuje
     */
    public CareTaker(Storage storage) {
        this.storage = storage;
    }

    /**
     * Uloženie stavov poličiek, bodov cesty a vozíkov.
     */
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

    /**
     * Načítanie stavov poličiek, bodov cesty a vozíkov.
     */
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
