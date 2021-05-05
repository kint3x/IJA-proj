package design.model.path;

import design.model.memento.State;

/**
 * Trieda predstavujúca stav bodu.
 */
public class PathPointState extends State {
    public Boolean isBlocked;

    /**
     * Konštruktor.
     * @param isBlocked prechodnosť bodu
     */
    public PathPointState (Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    /**
     * Získanie hlbokej kópie objektu.
     * @return kópia objektu
     */
    public PathPointState clone() {
        return new PathPointState(this.isBlocked);
    }
}
