package design.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.min;

public class Cart {
    private int maxItems;
    private int cartPosIndex;
    private ArrayList<Item> load;
    private boolean busy;
    private final Object busyLock = new Object();
    private PropertyChangeSupport support;
    private CartLoad cartLoad;
    private ArrayList<Request> requests = new ArrayList<>();

    /**
     * Cesta pre všetky vozíky.
     */
    private Path path;

    /**
     * Zoznam bodov predstavujúci cestu vozíka pre náklad.
     */
    private ArrayList<PathPoint> path2Shelf;

    /**
     * Zoznam bodov predstavujúci cestu vozíka na miesto výkladu.
     */
    private ArrayList<PathPoint> path2Drop;

    private Request request;

    /**
     * Konštruktor triedy Cart.
     * @param cartPosIndex počiatočný index
     * @param maxItems maximálny počet položiek, ktoré vozík naraz odnesie
     * @param path objekt cesty, po ktorom môže vozík chodiť
     */
    public Cart(int cartPosIndex, int maxItems, Path path) {
        this.cartPosIndex = cartPosIndex;
        this.path = path;
        this.maxItems = maxItems;
        this.support = new PropertyChangeSupport(this);
        this.cartLoad = null; // pridanie poc. bodu
    }

    /**
     * Vráti x-ovú pozícia vozíka. Možné zavolať len ak vozík ešte necestoval.
     * @return x-ová súradnica
     */
    public int getStartPosX() {
        return path.getPoints().get(getCartPosIndex()).getPosX();
    }

    /**
     * Pridanie požiadavku do zoznamu a prípadné rozdelenie na viacero požiadakov.
     * @param request požiadavok
     */
    public void addRequest(Request request) {
        int reqCount = request.getCount();

        while (reqCount > 0) {
            Request partialReq = new Request(request.getShelf(), min(reqCount, this.getMaxItems()), request.getItemType());
            this.getRequests().add(partialReq);
            reqCount -= this.getMaxItems();
        }

        this.request = request;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    /**
     * Vráti y-ovú pozícia vozíka. Možné zavolať len ak vozík ešte necestoval.
     * @return y-ová súradnica
     */
    public int getStartPosY() {
        return path.getPoints().get(getCartPosIndex()).getPosY();
    }

    /**
     * Vráti nosnosť vozíka
     * @return maximálne počet položiek
     */
    public int getMaxItems() {
        return this.maxItems;
    }

    public ArrayList<PathPoint> getPathPoints(){
        return this.path2Shelf;
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
    public void setCartPosIndex(int index, boolean drop) {
        if (!drop && index < path2Shelf.size()) {
            support.firePropertyChange("posX", -1, this.path2Shelf.get(index).getPosX());
            support.firePropertyChange("posY", -1, this.path2Shelf.get(index).getPosY());
        } else if (drop && index < path2Drop.size()) {
            support.firePropertyChange("posX", -1, this.path2Drop.get(index).getPosX());
            support.firePropertyChange("posY", -1, this.path2Drop.get(index).getPosY());
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
     */
    public void deliverRequest(Request request) {
        this.setBusy(true);
        this.request = request;
        new Thread(new CartThread(request)).start();
    }

    public Path getPath() {
        return this.path;
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
        public CartThread(Request request) {
            path2Shelf = new ArrayList<>();
        }

        private int calculatePath2Drop() {
            int dropIndex;

            PathPoint point = path2Drop.get(getCartPosIndex());
            path2Drop = new ArrayList<>();
            dropIndex = path.calculatePath2Drop(point.getPosX(), point.getPosY(), path2Drop);

            if (dropIndex == -1) {
                // pridanie aktualnej pozicie
                path2Drop = new ArrayList<>();
                path2Drop.add(point);
                System.err.format("Nemožno vyložiť %d kusov tovaru '%s' z dôvodu zablokovanej cesty. Vozík čaká na uvoľnenie cesty.\n", request.getCount(), request.getItemType().getName());
                return -1;
            } else {
                return dropIndex;
            }
        }

        private int calculatePath2Shelf() {
            int pickupIndex;

            Shelf shelf = request.getShelf();
            PathPoint point = path2Shelf.get(getCartPosIndex());
            path2Shelf = new ArrayList<>();

            pickupIndex = path.calculatePath2Shelf(shelf.getPosX(), shelf.getPosY(), point.getPosX(), point.getPosY(), path2Shelf);

            if (pickupIndex == -1) {
                // pridanie aktualnej pozicie
                path2Shelf = new ArrayList<>();
                path2Shelf.add(point);
                System.err.format("Nemožno naložiť %d kusov tovaru '%s' z dôvodu zablokovanej cesty. Vozík čaká na uvoľnenie cesty.\n", request.getCount(), request.getItemType().getName());
                return -1;
            } else {
                return pickupIndex;
            }
        }

        @Override
        public void run() {
            int pickupIndex = -1;
            int dropIndex = -1;
            int changeCounter;

            changeCounter = getPath().getChangeCounter() - 1;
            path2Shelf = new ArrayList<>();
            path2Shelf.add(path.getPoints().get(getCartPosIndex()));
            setCartPosIndex(0, false);

            // nalozenie tovaru
            while (getCartPosIndex() < path2Shelf.size()) {
                // cestovanie vozika
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                }

                // kontrola zmeny cesty
                if (getPath().getChangeCounter() != changeCounter) {
                    changeCounter = getPath().getChangeCounter();
                    pickupIndex = this.calculatePath2Shelf();
                    setCartPosIndex(0, false);

                    while (pickupIndex == -1) {
                        // caka na opatovnu zmenu cesty
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        // ak sa zmenila, skusi znovu prepocitat
                        if (getPath().getChangeCounter() != changeCounter) {
                            changeCounter = getPath().getChangeCounter();
                            pickupIndex = this.calculatePath2Shelf();
                        }
                    }

                    continue;
                }

                // nalozenie tovaru
                if (getCartPosIndex() == pickupIndex) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    request.getShelf().incrementHeatCounter();
                    load();
                    System.out.format("nakladam %d ks '%s'\n", request.getCount(), request.getItemType().getName());
                }

                setCartPosIndex(getCartPosIndex() + 1, false);
            }

            changeCounter = getPath().getChangeCounter() - 1;
            path2Drop = new ArrayList<>();
            path2Drop.add(path2Shelf.get(path2Shelf.size()-1));
            setCartPosIndex(0, true);

            while (getCartPosIndex() < path2Drop.size()) {
                // cestovanie vozika
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                }

                // kontrola zmeny cesty
                if (getPath().getChangeCounter() != changeCounter) {
                    changeCounter = getPath().getChangeCounter();
                    dropIndex = this.calculatePath2Drop();
                    setCartPosIndex(0, true);

                    while (dropIndex == -1) {
                        // caka na opatovnu zmenu cesty
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        // ak sa zmenila, skusi znovu prepocitat
                        if (getPath().getChangeCounter() != changeCounter) {
                            changeCounter = getPath().getChangeCounter();
                            dropIndex = this.calculatePath2Drop();
                        }
                    }

                    continue;
                }

                setCartPosIndex(getCartPosIndex() + 1, true);
            }

            unload();
            System.out.format("vykladam %d ks '%s'\n", request.getCount(), request.getItemType().getName());

            setBusy(false);

            // skusi vybavit dalsie cakajuce poziadavky
            path.deliverRequests();

            return;
        }
    }
}
