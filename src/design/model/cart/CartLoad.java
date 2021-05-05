package design.model.cart;

import design.model.item.Item;

/**
 * Trieda predstavujúca náklad vozíka (1 položku).
 */
public class CartLoad {
    private int count;
    private Item item;

    /**
     * Konštruktor.
     * @param count počet položiek
     * @param item položka
     */
    public CartLoad(int count, Item item) {
        this.count = count;
        this.item = item;
    }

    /**
     * Získanie položky.
     * @return položka
     */
    public Item getItem() {
        return item;
    }

    /**
     * Získanie počtu.
     * @return počet
     */
    public int getCount() {
        return count;
    }
}
