/**
 * Súbor obsahuje definíciu triedy Shelf.
 * @author Simon Košina
 */

package design.model.shelf;

import design.model.item.Item;
import design.model.item.ItemType;
import design.model.memento.Memento;
import design.model.memento.ObjectCareTaker;
import design.model.memento.Originator;
import design.model.memento.State;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trieda reprezentujúca regál, obsahujúci položky.
 */
public class Shelf implements Originator {
    private ObjectCareTaker shelfCareTaker = new ObjectCareTaker();
    private HashMap<String, Integer> itemCounts = new HashMap<>();
    private final int posX;
    private final int posY;
    private ShelfState state;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Konštruktor.
     * @param posX  x-ová súradnica
     * @param posY  y-ová súradnica
     */
    public Shelf(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.state = new ShelfState(0, new ArrayList<>());
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
     * Vráti čítač návštevnosti poličky.
     * @return čítač
     */
    public int getHeatCounter() {
        return this.state.heatCounter;
    }

    /**
     * Inkrementuje hodnotu čítača návštevnosti.
     */
    public void incrementHeatCounter() {
        this.state.heatCounter += 1;
    }

    /**
     * Pridanie observera.
     * @param pcl observer
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.support.addPropertyChangeListener(pcl);
    }

    /**
     * Odobranie observera.
     * @param pcl observer
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        this.support.removePropertyChangeListener(pcl);
    }

    /**
     * Získanie aktuálneho stavu regálu.
     * @return stav
     */
    @Override
    public State getState() {
        return this.state.clone();
    }

    /**
     * Nastavenie stavu regálu a prepočítanie položiek.
     * @param state nový stav
     */
    @Override
    public void setState(State state) {
        this.state = ((ShelfState) state).clone();
        support.firePropertyChange("items", null, null);
        this.updateCounts();
    }

    /**
     * Získanie správcu stavov daného objektu.
     * @return správca stavov
     */
    @Override
    public ObjectCareTaker getCareTaker() {
        return this.shelfCareTaker;
    }

    /**
     * Získanie stavu ako objekt Memento.
     * @return objekt s aktuálnym stavom
     */
    @Override
    public Memento saveStateToMemento() {
        return new Memento(this.getState());
    }

    /**
     * Nastavanie stavu z objektu Memento.
     * @param memento objekt s novým stavom
     */
    @Override
    public void setStateFromMemento(Memento memento) {
        this.setState((ShelfState) memento.getState());
    }

    /**
     * Prepočíta jednotlivé položky v regáli.
     */
    public void updateCounts() {
        itemCounts = new HashMap<>();

        for (Item item : this.state.items) {
            itemCounts.put(item.getType().getName(), itemCounts.getOrDefault(item.getType().getName(),0) + 1);
        }
    }

    /**
     * Upraví počty jednotlivých položiek.
     * @param itemName  meno položky
     * @param diff      hodnota, ktorá bude pripočítaná k aktuálnej
     */
    public void updateCounts(String itemName, int diff) {
        this.itemCounts.put(itemName, this.itemCounts.getOrDefault(itemName, 0) + diff);

        if (this.itemCounts.get(itemName) == 0) {
            this.itemCounts.remove(itemName);
        }
    }

    /**
     * Pridanie jednej položky do regálu.
     * @param item  položka
     */
    public void addItem(Item item) {
        this.state.items.add(item);
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
     * Vymaže všetky položky daného typu.
     * @param itemType typ položky
     */
    public void deleteItems(ItemType itemType) {
        Item item = this.removeItem(itemType);

        while (item != null) {
            item = this.removeItem(itemType);
        }
    }

    /**
     * Odobratie niekoľkých položiek z regálu.
     * @param itemType typ položky
     * @param count počet položiek
     */
    public void removeItem(ItemType itemType, int count) {
        for (int i = 0; i < count; i++) {
            this.removeItem(itemType);
        }
    }

    /**
     * Odstráni z poličky ľubovolnú položku daného typu.
     * @param itemType  typ položky
     * @return          odobraná položka
     */
    public Item removeItem(ItemType itemType) {
        int i = 0;

        for (; i < this.state.items.size(); i++) {
            if (this.state.items.get(i).getType().equals(itemType)) {
                break;
            }
        }

        if (i >= this.state.items.size()) {
            return null;
        } else {
            Item item =  this.state.items.remove(i);

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

    /**
     * Vráti objekt reprezetujúci stav skladu. Kľúčami sú názvy položiek a hodnotami sú počty.
     */
    public HashMap<String, Integer> getItemCounts(){
        return this.itemCounts;
    }

}
