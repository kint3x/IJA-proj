package design.model;

import java.util.ArrayList;

/**
 * Trieda reprezentujúca sklad.
 */
public class Storage {
    private ArrayList<Shelf> shelfs = new ArrayList<>();

    /**
     * Pridanie regálu do skladu.
     * @param shelf regál
     */
    public void addShelf(Shelf shelf) {
        shelfs.add(shelf);
    }

    /**
     * Vytvorí a pridá do skladu regále na pozíciach ohraníčených bodmi [x1,y1] a [x2, y2], vrátane.
     * @param x1    x-ová súradnica 1. bodu
     * @param y1    y-ová súradnica 1. bodu
     * @param x2    x-ová súradnica 2. bodu
     * @param y2    y-ová súradnica 2. bodu
     */
    public void createShelfs(int x1, int y1, int x2, int y2) {
        if (x2 < x1) {
            int temp;
            temp = x2;
            x2 = x1;
            x1 = temp;
        }

        if (y2 < y1) {
            int temp;
            temp = y2;
            y2 = y1;
            y1 = temp;
        }

        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                Shelf shelf = new Shelf(i,j);
                this.addShelf(shelf);
            }
        }
    }
}
