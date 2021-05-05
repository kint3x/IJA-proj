package design.model.memento;

public class Memento {
    private State state;

    public Memento(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}