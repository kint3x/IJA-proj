package design.model.memento;

/**
 * Trieda uchovávajúca jeden stav objektu.
 */
public class Memento {
    private State state;

    /**
     * Konštruktor.
     * @param state stav na uloženie
     */
    public Memento(State state) {
        this.state = state;
    }

    /**
     * Získanie uloženého stavu.
     * @return uložený stav
     */
    public State getState() {
        return state;
    }
}