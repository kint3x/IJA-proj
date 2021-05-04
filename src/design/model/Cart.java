package design.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.min;

public class Cart {
    private int maxItems;
    private int loadedItems = 0;
    private int cartPosIndex;
    private boolean busy;
    private final Object busyLock = new Object();
    private PropertyChangeSupport support;
    private ArrayList<CartLoad> cartLoad = new ArrayList<>();
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
    }

    /**
     * Vráti x-ovú pozícia vozíka. Možné zavolať len ak vozík ešte necestoval.
     * @return x-ová súradnica
     */
    public int getStartPosX() {
        return path.getPoints().get(getCartPosIndex()).getPosX();
    }

    public int getLoadedItems() {
        return loadedItems;
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
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public int getRequestsItemsCount() {
        int sum = 0;

        for (Request r : this.getRequests()) {
            sum += r.getCount();
        }

        return sum;
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

    public void load(Request request) {
        if (request.getCount() <= getMaxItems() - getLoadedItems()) {
            CartLoad cartLoad = new CartLoad(request.getCount(), new Item(request.getItemType()));
            support.firePropertyChange("load", this.getCartLoad(), cartLoad);
            this.getCartLoad().add(cartLoad);
            this.loadedItems += request.getCount();
        }
    }

    public void unload() {
        support.firePropertyChange("load", this.getCartLoad(), new ArrayList<>());
        this.loadedItems = 0;
        this.cartLoad = new ArrayList<>();
    }

    public ArrayList<CartLoad> getCartLoad() {
        return this.cartLoad;
    }

    /**
     * Pošle vozík vybaviť objednávky.
     */
    public void deliverRequests() {
        this.setBusy(true);
        new Thread(new CartThread()).start();
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

        private int calculatePath2Shelf(Request request) {
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

            path2Shelf = new ArrayList<>();
            path2Shelf.add(path.getPoints().get(getCartPosIndex()));

            while (getRequests().size() > 0) {
                Request request = getRequests().get(0);


                if (request.getCount() > getMaxItems() - getLoadedItems()) {
                    // prekrocena kapacita
                    break;
                }

                PathPoint startPoint = path2Shelf.get(path2Shelf.size()-1);
                changeCounter = getPath().getChangeCounter() - 1;
                path2Shelf = new ArrayList<>();
                path2Shelf.add(startPoint);
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
                        pickupIndex = this.calculatePath2Shelf(request);
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
                                pickupIndex = this.calculatePath2Shelf(request);
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
                        load(request);
                        getRequests().remove(0);
                        System.out.format("nakladam %d ks '%s'\n", request.getCount(), request.getItemType().getName());
                    }

                    setCartPosIndex(getCartPosIndex() + 1, false);
                }
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

            for (CartLoad cl : getCartLoad()) {
                System.out.format("vykladam %d ks '%s'\n", cl.getCount(), cl.getItem().getType().getName());
            }

            unload();

            // reset pozicie
            path2Shelf = new ArrayList<>();
            path2Shelf.add(path2Drop.get(path2Drop.size()-1));
            setCartPosIndex(0, false);

            setBusy(false);

            // skusi vybavit dalsie cakajuce poziadavky
            path.deliverRequests();

            return;
        }
    }
}
