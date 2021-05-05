package design.model.path;

import design.model.memento.Memento;
import design.model.memento.ObjectCareTaker;
import design.model.memento.Originator;
import design.model.memento.State;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * Trieda predstavujúca jeden bod cesty.
 */
public class PathPoint implements Originator {
    private ObjectCareTaker pathCareTaker = new ObjectCareTaker();
    private int posX;
    private int posY;
    private Boolean isBlocked;
    private PathPointState state;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Konštruktor.
     * @param posX x-ová súrdanica bodu
     * @param posY y-ová súrdanica bodu
     */
    public PathPoint(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.isBlocked = false;
        this.state = new PathPointState(false);
    }

    /**
     * Získanie pozície bodu.
     * @return x-ová súradnica
     */
    public int getPosX() {
        return this.posX;
    }

    /**
     * Získanie pozície bodu.
     * @return y-ová súradnica
     */
    public int getPosY() {
        return this.posY;
    }

    /**
     * Zmena stavu zablokovania bodu.
     */
    public void switchBlocked() {
        this.isBlocked = !this.isBlocked();
    }

    /**
     * Zistí prechodnosť bodu.
     * @return true ak bod je zablokovaný, inak false
     */
    public Boolean isBlocked() {
        return this.isBlocked;
    }

    /**
     * Výpis bodu, vhodný pre testovanie.
     */
    public void printPoint() {
        System.out.format("[%d, %d]\n", this.getPosX(), this.getPosY());
    }

    /**
     * Získanie stavu objektu.
     * @return stav
     */
    @Override
    public State getState() {
        return this.state;
    }

    /**
     * Nastavenie stavu objektu.
     * @param state nový stav
     */
    @Override
    public void setState(State state) {
        PathPointState pState = (PathPointState) state;
        support.firePropertyChange("block", this.isBlocked(), pState.isBlocked);
        this.state = (PathPointState) state;
    }

    /**
     * Získanie stavu ako objekt Memento.
     * @return objekt s aktuálnym stavom
     */
    @Override
    public Memento saveStateToMemento() {
        return new Memento(this.getState());
    }

    /**
     * Nastavanie stavu z objektu Memento.
     * @param memento objekt s novým stavom
     */
    @Override
    public void setStateFromMemento(Memento memento) {
        this.setState(memento.getState());
    }

    /**
     * Získanie správcu stavov daného objektu.
     * @return správca stavov
     */
    @Override
    public ObjectCareTaker getCareTaker() {
        return this.pathCareTaker;
    }

    /**
     * Pridanie observera.
     * @param pcl observer
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.support.addPropertyChangeListener(pcl);
    }

    /**
     * Odobranie observera.
     * @param pcl observer
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        this.support.removePropertyChangeListener(pcl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathPoint point = (PathPoint) o;
        return getPosX() == point.getPosX() && getPosY() == point.getPosY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosX(), getPosY());
    }
}
