package design.model;

public class CartMemento {
    private CartState state;

    public CartMemento(CartState state) {
        this.state = state;
    }

    public CartState getState() {
        return state;
    }
}