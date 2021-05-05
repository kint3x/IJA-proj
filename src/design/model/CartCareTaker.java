package design.model;

import java.util.ArrayList;

public class CartCareTaker {
    private ArrayList<CartMemento> mementoList = new ArrayList<>();

    public void addState(CartMemento cartMemento) {
        this.mementoList.add(cartMemento);
    }

    public CartMemento getState(int index) {
        return this.mementoList.get(index);
    }
}
