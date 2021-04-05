package design.model;

import java.util.ArrayList;

/**
 * Trieda reprezentujúca regál, obsahujúci položky.
 */
public class Shelf {
    private ArrayList<Item> items = new ArrayList<>();

    /**
     * Pridanie položky do regálu.
     * @param item  položka
     */
    public void addItem(Item item) {
        this.items.add(item);
    }

    /**
     * Odstráni z poličky ľubovolnú položku daného typu.
     * @param itemType  typ položky
     * @return          odobraná položka
     */
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

    /**
     * Zistí počet položiek daného typu v regáli.
     * @param itemType  typ položiek
     * @return          počet položiek daného typu
     */
    public int countItems(ItemType itemType) {
        int cnt = 0;

        for (Item item : this.items) {
            if (item.getType().equals(itemType)) {
                cnt++;
            }
        }

        return cnt;
    }

    public void printShelf() {
        System.out.format("x: %d, y: %d\n", this.getPosX(), this.getPosY());

        for (Item i : this.items) {
            System.out.format("\tname: %s, count: %d\n", i.getType().getName(), this.countItems(i.getType()));
        }

        System.out.println("------------------------------");
    }
}
