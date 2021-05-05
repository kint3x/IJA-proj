package design.model.shelf;

import design.model.item.Item;
import design.model.memento.State;

import java.util.ArrayList;

/**
 * Trieda predstavujúca stav regálu.
 */
public class ShelfState extends State {
    public int heatCounter;
    public ArrayList<Item> items;

    /**
     * Konštruktor.
     * @param heatCounter počet zastávok pri danom regále
     * @param items zoznam položiek v regále
     */
    public ShelfState(int heatCounter, ArrayList<Item> items) {
        this.heatCounter = heatCounter;
        this.items = new ArrayList<>();
        this.items.addAll(items);
    }

    /**
     * Získanie hlbokej kópie objektu.
     * @return kópia objektu
     */
    public ShelfState clone() {
        return new ShelfState(this.heatCounter, this.items);
    }
}
