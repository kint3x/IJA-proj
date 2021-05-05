package design.model.cart;

import design.model.item.Item;

public class CartLoad {
    private int count;
    private Item item;

    public CartLoad(int Count, Item item) {
        this.count = Count;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }
}
