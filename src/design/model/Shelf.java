package design.model;

import java.util.ArrayList;

public class Shelf {
    private ArrayList<Item> items = new ArrayList<>();
    private Integer posX;
    private Integer posY;

    public Shelf(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public Item removeItem(ItemType itemType) {
        int i = 0;

        for (; i < this.items.size(); i++) {
            if (this.items.get(i).getType().equals(itemType)) {
                break;
            }
        }

        if (i >= this.items.size()) {
            return null;
        } else {
            return this.items.remove(i);
        }
    }

    public int countItems(ItemType itemType) {
        int cnt = 0;

        for (Item item : this.items) {
            if (item.getType().equals(itemType)) {
                cnt++;
            }
        }

        return cnt;
    }
}
