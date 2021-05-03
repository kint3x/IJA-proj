package design.model;

public class CartLoad {
    private int count;
    private Item item;

    public CartLoad(int Count, Item item) {
        this.count = count;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }
}
