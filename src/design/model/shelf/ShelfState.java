package design.model.shelf;

import design.model.item.Item;
import design.model.memento.State;

import java.util.ArrayList;

public class ShelfState extends State {
    public int heatCounter;
    public ArrayList<Item> items;

    public ShelfState(int heatCounter, ArrayList<Item> items) {
        this.heatCounter = heatCounter;
        this.items = new ArrayList<>();
        this.items.addAll(items);
    }

    public ShelfState clone() {
        return new ShelfState(this.heatCounter, this.items);
    }
}
