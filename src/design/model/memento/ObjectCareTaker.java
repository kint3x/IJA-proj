package design.model.memento;

import java.util.ArrayList;

public class ObjectCareTaker {
    private ArrayList<Memento> mementoList = new ArrayList<>();

    public void addState(Memento memento) {
        this.mementoList.add(memento);
    }

    public Memento getState(int index) {
        return this.mementoList.get(index);
    }
}
