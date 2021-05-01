package design.model;

import java.util.ArrayList;

public class Cart {
    private int pathPointIndex;
    private int maxItems;
    private ArrayList<Item> load;

    /**
     * Konštruktor triedy Cart.
     * @param maxItems maximálny počet položiek, ktoré vozík naraz odnesie
     */
    public Cart(int pathPointIndex, int maxItems) {
        this.pathPointIndex = pathPointIndex;
        this.maxItems = maxItems;
    }
}
