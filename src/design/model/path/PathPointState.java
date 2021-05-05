package design.model.path;

import design.model.memento.State;

public class PathPointState extends State {
    public Boolean isBlocked;

    public PathPointState (Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public PathPointState clone() {
        return new PathPointState(this.isBlocked);
    }
}
