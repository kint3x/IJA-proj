package design.model.memento;

import java.util.ArrayList;

/**
 * Trieda spravujúca ukladanie stavov jedného objektu.
 */
public class ObjectCareTaker {
    private ArrayList<Memento> mementoList = new ArrayList<>();

    /**
     * Pridanie stavu.
     * @param memento stav
     */
    public void addState(Memento memento) {
        this.mementoList.add(memento);
    }

    /**
     * Získanie stavu.
     * @param index poradie uloženého stavu
     * @return stav
     */
    public Memento getState(int index) {
        return this.mementoList.get(index);
    }

    /**
     * Vráti počet uložených stavov.
     * @return počet uložených stavov
     */
    public int getStatesCount() {
        return this.mementoList.size();
    }
}
