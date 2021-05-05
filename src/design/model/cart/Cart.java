package design.model.cart;

import design.controllers.StorageController;
import design.model.*;
import design.model.item.Item;
import design.model.memento.Memento;
import design.model.memento.ObjectCareTaker;
import design.model.path.Path;
import design.model.path.PathPoint;
import design.model.shelf.Shelf;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.min;

public class Cart {
    private ObjectCareTaker cartCareTaker = new ObjectCareTaker();
    private int maxItems;
    private final Object busyLock = new Object();
    private PropertyChangeSupport support;
    private ArrayList<PathPoint> travelledPoints = new ArrayList<>();
    private CartState state;
    private final Object stateLock = new Object();
    private CartThread cartThread;

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

    /**
     * Konštruktor triedy Cart.
     * @param cartPosIndex počiatočný index
     * @param maxItems maximálny počet položiek, ktoré vozík naraz odnesie
     * @param path objekt cesty, po ktorom môže vozík chodiť
     */
    public Cart(int cartPosIndex, int maxItems, Path path) {
        this.state = new CartState(path.getPoints().get(cartPosIndex), new ArrayList<CartLoad>(), new ArrayList<Request>(),
                false, cartPosIndex, new ArrayList<PathPoint>());
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

    public ObjectCareTaker getCareTaker() {
        return this.cartCareTaker;
    }

    /**
     * Vráti y-ovú pozícia vozíka. Možné zavolať len ak vozík ešte necestoval.
     * @return y-ová súradnica
     */
    public int getStartPosY() {
        return path.getPoints().get(getCartPosIndex()).getPosY();
    }

    public int getLoadedItems() {
        int sum = 0;

        for (CartLoad load : this.getCartLoad()) {
            sum += load.getCount();
        }

        return sum;
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

    public void setState(CartState state) {
        synchronized (stateLock) {
            cartThread.stop();
            this.state = (CartState) state.clone();

            support.firePropertyChange("posX", -1, state.position.getPosX());
            support.firePropertyChange("posY", -1, state.position.getPosY());
            support.firePropertyChange("load", null, getCartLoad());

            if (state.busy) {
                deliverRequests();
            }
        }
    }

    public CartState getState() {
        synchronized (stateLock) {
            return (CartState) this.state.clone();
        }
    }

    public Memento saveStateToMemento() {
        return new Memento(this.getState());
    }

    public void setStateFromMemento(Memento memento) {
        this.setState((CartState) memento.getState());
    }

    public ArrayList<Request> getRequests() {
        synchronized (stateLock) {
            return this.state.requests;
        }
    }

    public int getRequestsItemsCount() {
        int sum = 0;

        for (Request r : this.getRequests()) {
            sum += r.getCount();
        }

        return sum;
    }

    public void removeRequest() {
        synchronized (stateLock) {
            this.state.requests.remove(0);
        }
    }

    /**
     * Vráti nosnosť vozíka
     * @return maximálne počet položiek
     */
    public int getMaxItems() {
        return this.maxItems;
    }

    public ArrayList<PathPoint> getPath2Shelf() {
        return this.path2Shelf;
    }

    public void addPath2Shelf(PathPoint point) {
        this.path2Shelf.add(point);
    }

    public ArrayList<PathPoint> getPath2Drop() {
        return  this.path2Drop;
    }

    public void addPath2Drop(PathPoint point) {
        this.path2Drop.add(point);
    }

    public void setPath2Shelf(ArrayList<PathPoint> path2Shelf) {
        this.path2Shelf = path2Shelf;
    }

    public void setPath2Drop(ArrayList<PathPoint> path2Drop) {
        this.path2Drop = path2Drop;
    }

    public PathPoint getCartPoint() {
        synchronized (stateLock) {
            return this.state.position;
        }
    }

    public void setCartPoint(PathPoint point) {
        synchronized (stateLock) {
            this.state.position = point;
        }
    }

    /**
     * Vráti aktuálnu plánovanú cestu vozíka.
     * @return zoznam bodov cesty
     */
    public ArrayList<PathPoint> getPathPoints(){
        ArrayList<PathPoint> pathPoints = new ArrayList<>(getTravelledPoints());

        if (getPath2Shelf() != null && getTravelledPoints().size() == 0) {
            for (int i = getCartPosIndex(); i < getPath2Shelf().size(); i++) {
                pathPoints.add(getPath2Shelf().get(i));
            }
        } else if (getPath2Drop() != null && getPath2Drop().size() == 0) {
            for (int i = getCartPosIndex(); i < getPath2Drop().size(); i++) {
                pathPoints.add(getPath2Drop().get(i));
            }
        }

        return pathPoints;
    }

    public ArrayList<PathPoint> getTravelledPoints() {
        synchronized (stateLock) {
            return this.state.travelledPoints;
        }
    }

    public void addTravelledPoints(PathPoint point) {
        synchronized (stateLock) {
            this.state.travelledPoints.add(point);
        }
    }

    public void setTravelledPoints(ArrayList<PathPoint> points) {
        synchronized (stateLock) {
            this.state.travelledPoints = new ArrayList<>(points);
        }
    }

    /**
     * Zistí či vozík akurát doručuje zásielku
     * @return true, ak vozík práve doručuje zásielku, inak false
     */
    public boolean getBusy() {
        synchronized (stateLock) {
            synchronized (this.busyLock) {
                return this.state.busy;
            }
        }
    }

    /**
     * Nastavenie premennej busy.
     * @param busy nová hodnota
     */
    public void setBusy(boolean busy) {
        synchronized (stateLock) {
            synchronized (this.busyLock) {
                this.state.busy = busy;
            }
        }
    }

    /**
     * Vráti index do zoznamu bodov, reprezentujúci aktuálnu polohu vozíka.
     * @return index
     */
    public int getCartPosIndex() {
        synchronized (stateLock) {
            return this.state.index;
        }
    }

    /**
     * Nastavenie nového indexu a notifikácia observerov.
     * @param index index
     */
    public void setCartPosIndex(int index, boolean drop) {
        if (!drop && index < this.getPath2Shelf().size()) {
            this.addTravelledPoints(getCartPoint());
            setCartPoint(this.getPath2Shelf().get(index));
            support.firePropertyChange("posX", -1, this.getPath2Shelf().get(index).getPosX());
            support.firePropertyChange("posY", -1, this.getPath2Shelf().get(index).getPosY());
        } else if (drop && index < this.getPath2Drop().size()) {
            this.addTravelledPoints(getCartPoint());
            setCartPoint(this.getPath2Drop().get(index));
            support.firePropertyChange("posX", -1, this.getPath2Drop().get(index).getPosX());
            support.firePropertyChange("posY", -1, this.getPath2Drop().get(index).getPosY());
        }

        synchronized (stateLock) {
            this.state.index = index;
        }
    }

    public void load(Request request) {
        if (request.getCount() <= getMaxItems() - getLoadedItems()) {
            this.addCartLoad(new CartLoad(request.getCount(), new Item(request.getItemType())));
            support.firePropertyChange("load", null, getCartLoad());
        }
    }

    public void unload() {
        this.setCartLoad(new ArrayList<>());
        support.firePropertyChange("load", null, getCartLoad());
    }

    public ArrayList<CartLoad> getCartLoad() {
        synchronized (stateLock) {
            return this.state.load;
        }
    }

    public void addCartLoad(CartLoad load) {
       synchronized (stateLock) {
            this.state.load.add(load);
       }
    }

    public void setCartLoad(ArrayList<CartLoad> load) {
        synchronized (stateLock) {
            this.state.load = load;
        }
    }

    /**
     * Pošle vozík vybaviť objednávky.
     */
    public void deliverRequests() {
        this.setBusy(true);
        this.cartThread = new CartThread();
        this.cartThread.start();
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
        public Thread worker;
        private final AtomicBoolean running = new AtomicBoolean(false);

        private int calculatePath2Drop() {
            int dropIndex;
            ArrayList<PathPoint> path2Drop = new ArrayList<>();

            dropIndex = path.calculatePath2Drop(getCartPoint().getPosX(), getCartPoint().getPosY(), path2Drop);

            setPath2Drop(path2Drop);

            if (dropIndex == -1) {
                System.err.format("Nemožno vyložiť %d kusov tovaru.\n", getLoadedItems());
                return -1;
            } else {
                return dropIndex;
            }
        }

        private int calculatePath2Shelf(Request request) {
            int pickupIndex;

            Shelf shelf = request.getShelf();
            ArrayList<PathPoint> path2Shelf = new ArrayList<>();
            pickupIndex = path.calculatePath2Shelf(shelf.getPosX(), shelf.getPosY(), getCartPoint().getPosX(), getCartPoint().getPosY(), path2Shelf);
            setPath2Shelf(path2Shelf);

            if (pickupIndex == -1) {
                System.err.format("Nemožno naložiť %d kusov tovaru '%s' z dôvodu zablokovanej cesty. Vozík čaká na uvoľnenie cesty.\n", request.getCount(), request.getItemType().getName());
                return -1;
            } else {
                return pickupIndex;
            }
        }

        public void start() {
            worker = new Thread(this);
            worker.setDaemon(true);
            worker.start();
        }

        public void stop() {
            running.set(false);
        }

        @Override
        public void run() {
            running.set(true);

            int pickupIndex = -1;
            int dropIndex = -1;
            int changeCounter;

            setPath2Shelf(new ArrayList<>());
            setPath2Drop(new ArrayList<>());

            while (getRequests().size() > 0 && running.get()) {
                Request request = getRequests().get(0);

                if (request.getCount() > getMaxItems() - getLoadedItems()) {
                    // prekrocena kapacita
                    break;
                }

                changeCounter = getPath().getChangeCounter() - 1;
                setPath2Shelf(new ArrayList<>());
                addPath2Shelf(getCartPoint());
                setCartPosIndex(0, false);

                // nalozenie tovaru
                while (getCartPosIndex() < getPath2Shelf().size() && running.get()) {
                    // cestovanie vozika
                    try {
                        Thread.sleep((long) (500 / StorageController.getSpeed()));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (!running.get()) {
                        return;
                    }

                    // pauza?
                    getPath().getCartsLock().lock();
                    getPath().getCartsLock().unlock();

                    // kontrola zmeny cesty
                    if (getPath().getChangeCounter() != changeCounter) {
                        changeCounter = getPath().getChangeCounter();
                        pickupIndex = this.calculatePath2Shelf(request);
                        setCartPosIndex(0, false);

                        while (pickupIndex == -1) {
                            // caka na opatovnu zmenu cesty
                            try {
                                Thread.sleep((long) (5000 / StorageController.getSpeed()));
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (!running.get()) {
                                return;
                            }

                            // pauza?
                            getPath().getCartsLock().lock();
                            getPath().getCartsLock().unlock();

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
                            Thread.sleep((long) (1000 / StorageController.getSpeed()));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (!running.get()) {
                            return;
                        }

                        // pauza?
                        getPath().getCartsLock().lock();
                        getPath().getCartsLock().unlock();

                        request.getShelf().incrementHeatCounter();
                        load(request);
                        removeRequest();
                        System.out.format("nakladam %d ks '%s'\n", request.getCount(), request.getItemType().getName());
                    }

                    setCartPosIndex(getCartPosIndex() + 1, false);
                }
            }

            setPath2Shelf(new ArrayList<>());

            changeCounter = getPath().getChangeCounter() - 1;
            addPath2Drop(getCartPoint());
            setCartPosIndex(0, true);

            while (getCartPosIndex() < getPath2Drop().size() && running.get()) {
                // cestovanie vozika
                try {
                    Thread.sleep((long) (500 / StorageController.getSpeed()));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (!running.get()) {
                    return;
                }

                // pauza?
                getPath().getCartsLock().lock();
                getPath().getCartsLock().unlock();

                // kontrola zmeny cesty
                if (getPath().getChangeCounter() != changeCounter) {
                    changeCounter = getPath().getChangeCounter();
                    dropIndex = this.calculatePath2Drop();
                    setCartPosIndex(0, true);

                    while (dropIndex == -1) {
                        // caka na opatovnu zmenu cesty
                        try {
                            Thread.sleep((long) (5000 / StorageController.getSpeed()));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (!running.get()) {
                            return;
                        }

                        // pauza?
                        getPath().getCartsLock().lock();
                        getPath().getCartsLock().unlock();

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

            // pauza?
            getPath().getCartsLock().lock();
            getPath().getCartsLock().unlock();

            for (CartLoad cl : getCartLoad()) {
                System.out.format("vykladam %d ks '%s'\n", cl.getCount(), cl.getItem().getType().getName());
            }

            try {
                Thread.sleep((long) (1000 / StorageController.getSpeed()));
            } catch (InterruptedException ex) {
                Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (!running.get()) {
                return;
            }

            unload();

            // reset pozicie
            setCartPosIndex(0, false);
            setPath2Drop(new ArrayList<>());
            setTravelledPoints(new ArrayList<>());

            setBusy(false);

            // skusi vybavit dalsie cakajuce poziadavky
            path.deliverRequests();
        }
    }
}
