/**
 * Súbor obsahuje definíciu triedy Shelf.
 * @author Simon Košina
 */

package design.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trieda reprezentujúca regál, obsahujúci položky.
 */
public class Shelf {
    private ArrayList<Item> items = new ArrayList<>();
    private HashMap<String, Integer> itemCounts = new HashMap<>();
    private int posX;
    private int posY;

    /**
     * Konštruktor.
     * @param posX  x-ová súradnica
     * @param posY  y-ová súradnica
     */
    public Shelf(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Vráti pozíciu regálu na x-ovej súradnici.
     * @return  hodnota x-ovej súradnice
     */
    public int getPosX() {
        return this.posX;
    }

    /**
     * Vráti pozíciu regálu na y-ovej súradnici.
     * @return  hodnota y-ovej súradnice
     */
    public int getPosY() {
        return this.posY;
    }

    /**
     * Upraví počty jednotlivých položiek.
     * @param itemName  meno položky
     * @param diff      hodnota, ktorá bude pripočítaná k aktuálnej
     */
    public void updateCounts(String itemName, int diff) {
        this.itemCounts.put(itemName, this.itemCounts.getOrDefault(itemName, 0) + diff);
    }

    /**
     * Pridanie jednej položky do regálu.
     * @param item  položka
     */
    public void addItem(Item item) {
        this.items.add(item);
        this.updateCounts(item.getType().getName(), 1);
    }

    /**
     * Pridanie niekoľkých položiek do regálu.
     * @param item  položka
     * @param count počet položiek
     */
    public void addItem(Item item, int count) {
        for (int i = 0; i < count; i++) {
            this.addItem(item);
        }
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
            Item item =  this.items.remove(i);
            this.updateCounts(item.getType().getName(), -1);
            return item;
        }
    }

    /**
     * Zistí počet položiek daného typu v regáli.
     * @param itemType  typ položiek
     * @return          počet položiek daného typu
     */
    public int countItems(ItemType itemType) {
        return this.itemCounts.getOrDefault(itemType.getName(), 0);
    }

    /**
     * Zistí počet položiek daného typu v regáli.
     * @param itemName  názov položiek
     * @return          počet položiek daného typu
     */
    public int countItems(String itemName) {
        return this.itemCounts.getOrDefault(itemName, 0);
    }

    /**
     * Vypíše informácie o regáli na štandardný výstup. Slúži na testovanie.
     */
    public void printShelf() {
        System.out.format("x: %d, y: %d\n", this.getPosX(), this.getPosY());

        for (String key : this.itemCounts.keySet()) {
            System.out.format("\tname: %s, count: %d\n", key, this.countItems(key));
        }

        System.out.println("------------------------------");
    }
}
