package design.model;

import java.util.ArrayList;

public class CareTaker {
    public Storage storage;
    public ArrayList<CartCareTaker> cartCareTakers = new ArrayList<>();

    public CareTaker(Storage storage) {
        this.storage = storage;
    }

    public void saveState() {
        for (Cart cart : this.storage.getPath().getCarts()) {
            cart.getCartCareTaker().addState(cart.saveStateToMemento());
        }
    }

    public void setState(int index) {
        for (Cart cart : this.storage.getPath().getCarts()) {
            cart.setStateFromMemento(cart.getCartCareTaker().getState(index));
        }
    }
}
