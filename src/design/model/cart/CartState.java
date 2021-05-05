package design.model.cart;

import design.model.path.PathPoint;
import design.model.Request;
import design.model.memento.State;

import java.util.ArrayList;

/**
 * Trieda predstavujúca stav vozíka.
 */
public class CartState extends State {
    public PathPoint position;
    public ArrayList<CartLoad> load;
    public ArrayList<Request> requests;
    public boolean busy;
    public int index;
    public ArrayList<PathPoint> travelledPoints;

    /**
     * Konštruktor.
     * @param position pozícia ako bod
     * @param load aktuálny náklad
     * @param requests nevybavených objednávok
     * @param busy flag zaneprázdnenosti vozíka
     * @param index index do zoznamu bodov
     * @param travelledPoints zoznam prejdených bodov
     */
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

    /**
     * Vytvorenie hlbokej kópie objektu.
     * @return kópia objektu
     */
    @Override
    public CartState clone() {
        return new CartState(this.position, this.load, this.requests, this.busy, this.index, this.travelledPoints);
    }
}
