package design.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cart {
    private int maxItems;
    private int cartPosIndex;
    private ArrayList<Item> load;
    private boolean busy;
    private final Object busyLock = new Object();
    private PropertyChangeSupport support;
    private CartLoad cartLoad;

    /**
     * Cesta pre všetky vozíky.
     */
    private Path path;

    /**
     * Zoznam bodov predstavujúci cestu vozíka pre náklad a späť.
     */
    private ArrayList<PathPoint> pathPoints;

    private Request request;

    /**
     * Konštruktor triedy Cart.
     * @param maxItems maximálny počet položiek, ktoré vozík naraz odnesie
     */
    public Cart(int cartPosIndex, int maxItems) {
        this.cartPosIndex = cartPosIndex;
        this.maxItems = maxItems;
        this.support = new PropertyChangeSupport(this);
        this.cartLoad = null;
    }

    /**
     * Vráti nosnosť vozíka
     * @return maximálne počet položiek
     */
    public int getMaxItems() {
        return this.maxItems;
    }

    /**
     * Zistí či vozík akurát doručuje zásielku
     * @return true, ak vozík práve doručuje zásielku, inak false
     */
    public boolean getBusy() {
        synchronized (this.busyLock) {
            return this.busy;
        }
    }

    /**
     * Nastavenie premennej busy.
     * @param busy nová hodnota
     */
    public void setBusy(boolean busy) {
        synchronized (this.busyLock) {
            this.busy = busy;
        }
    }

    /**
     * Vráti index do zoznamu bodov, reprezentujúci aktuálnu polohu vozíka.
     * @return index
     */
    public int getCartPosIndex() {
        return cartPosIndex;
    }

    /**
     * Nastavenie nového indexu a notifikácia observerov.
     * @param index index
     */
    public void setCartPosIndex(int index) {
        if (index < pathPoints.size()) {
            support.firePropertyChange("posX", this.pathPoints.get(this.cartPosIndex).getPosX(), this.pathPoints.get(index).getPosX());
            support.firePropertyChange("posY", this.pathPoints.get(this.cartPosIndex).getPosY(), this.pathPoints.get(index).getPosY());
        }

        this.cartPosIndex = index;
    }

    public void load() {
        if (request.getCount() <= maxItems) {
            CartLoad cartLoad = new CartLoad(request.getCount(), new Item(request.getItemType()));
            support.firePropertyChange("load", this.getCartLoad(), cartLoad);
            this.cartLoad = cartLoad;
        }
    }

    public void unload() {
        support.firePropertyChange("load", this.getCartLoad(), null);
        this.cartLoad = null;
    }

    public CartLoad getCartLoad() {
        return this.cartLoad;
    }

    /**
     * Pošle vozík vybaviť objednávku.
     * @param request objednávka
     * @param path objekt cesty, po ktorom môže vozík chodiť
     */
    public void deliverRequest(Request request, Path path) {
        this.setBusy(true);
        this.path = path;
        this.request = request;
        new Thread(new CartThread(path, request)).start();
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

    class CartThread implements Runnable {
        public CartThread(Path path, Request request) {
            pathPoints = new ArrayList<>();
            pathPoints.add(path.getPoints().get(getCartPosIndex())); // pridanie poc. bodu
        }

        private int calculatePath() {
            int pickupIndex;

            Shelf shelf = request.getShelf();
            PathPoint point = pathPoints.get(getCartPosIndex());
            pathPoints = new ArrayList<>();

            pickupIndex = path.calculatePath(shelf.getPosX(), shelf.getPosY(), point.getPosX(), point.getPosY(), pathPoints);

            if (pickupIndex == -1) {
                System.err.format("Nemožno doručiť %d kusov tovaru '%s' z dôvodu neexistujúcej cesty.\n", request.getCount(), request.getItemType().getName());
                return -1;
            } else {
                return pickupIndex;
            }
        }

        @Override
        public void run() {
            int pickupIndex;
            int changeCounter;

            changeCounter = path.getChangeCounter();
            pickupIndex = this.calculatePath();

            while (getCartPosIndex() < pathPoints.size()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (path.getChangeCounter() != changeCounter) {
                    System.out.println("zmena cesty");
                    changeCounter = path.getChangeCounter();
                    pickupIndex = this.calculatePath();

                    if (pickupIndex == -1) {
                        return;
                    }

                    continue;
                }

                if (getCartPosIndex() == pickupIndex) {
                    load();
                    System.out.format("nakladam %d ks '%s'\n", request.getCount(), request.getItemType().getName());
                }

                pathPoints.get(getCartPosIndex()).printPoint();
                setCartPosIndex(getCartPosIndex() + 1);
            }

            unload();
            System.out.format("vykladam %d ks '%s'\n", request.getCount(), request.getItemType().getName());

            setBusy(false);
            return;
        }
    }
}
