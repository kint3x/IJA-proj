package design.model;

public class Item {
    private ItemType type;

    public Item(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return this.type;
    }
}
