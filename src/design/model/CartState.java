package design.model;

import java.util.ArrayList;

public class CartState implements Cloneable {
    public PathPoint position;
    public ArrayList<CartLoad> load;
    public ArrayList<Request> requests;
    public boolean busy;
    public int index;
    public ArrayList<PathPoint> travelledPoints;

    public CartState (PathPoint position, ArrayList<CartLoad> load, ArrayList<Request> requests, boolean busy, int index, ArrayList<PathPoint> travelledPoints) {
        this.position = new PathPoint(position.getPosX(), position.getPosY());

        this.load = new ArrayList<>();
        this.load.addAll(load);

        this.requests = new ArrayList<>();
        this.requests.addAll(requests);

        this.busy = busy;

        this.index = index;

        this.travelledPoints = new ArrayList<>();
        this.travelledPoints.addAll(travelledPoints);
    }

    @Override
    public Object clone() {
        return new CartState(this.position, this.load, this.requests, this.busy, this.index, this.travelledPoints);
    }
}
