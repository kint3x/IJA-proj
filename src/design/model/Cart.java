package design.model;

import java.util.ArrayList;

public class Cart {
    private Integer pathPointIndex;
    private Integer maxItems;
    private ArrayList<Item> load;

    /**
     * Konštruktor triedy Cart.
     * @param maxItems maximálny počet položiek, ktoré vozík naraz odnesie
     */
    public Cart(Integer pathPointIndex, Integer maxItems) {
        this.pathPointIndex = pathPointIndex;
        this.maxItems = maxItems;
    }
}
