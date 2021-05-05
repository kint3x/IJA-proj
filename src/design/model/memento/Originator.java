package design.model.memento;

public interface Originator {

    void setState(State state);

    State getState();

    ObjectCareTaker getCareTaker();

    Memento saveStateToMemento();

    void setStateFromMemento(Memento memento);
}
