package design.model.memento;

/**
 * Rozhranie ktoré musia spĺňať objekty, ktorých stav sa uchováva.
 */
public interface Originator {

    /**
     * Nastavenie stavu.
     * @param state nový stav
     */
    void setState(State state);

    /**
     * Získanie stavu.
     * @return aktuálny stav
     */
    State getState();

    /**
     * Získanie správcu stavov daného objektu.
     * @return správca stavov
     */
    ObjectCareTaker getCareTaker();

    /**
     * Získanie stavu ako objekt Memento.
     * @return objekt s aktuálnym stavom
     */
    Memento saveStateToMemento();

    /**
     * Nastavanie stavu z objektu Memento.
     * @param memento objekt s novým stavom
     */
    void setStateFromMemento(Memento memento);
}
