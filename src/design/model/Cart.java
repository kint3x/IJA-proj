package design.model;

import java.util.ArrayList;

public class Cart {
    private int maxItems;
    private int cartPosIndex;
    private ArrayList<Item> load;
    private boolean busy;
    private final Object busyLock = new Object();


    /**
     * Konštruktor triedy Cart.
     * @param maxItems maximálny počet položiek, ktoré vozík naraz odnesie
     */
    public Cart(int cartPosIndex, int maxItems) {
        this.cartPosIndex = cartPosIndex;
        this.maxItems = maxItems;
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

    public void deliverRequest(Request request, Path path) {
        this.setBusy(true);

        new Thread(new CartThread(path, request, cartPosIndex));
    }

    class CartThread implements Runnable {
        private Path path;
        private ArrayList<PathPoint> pathPoints;
        private Request request;
        private int pointIndex;
        private int pickupIndex;

        public CartThread(Path path, Request request, int startIndex) {
            this.path = path;
            this.request = request;

            Shelf shelf = request.getShelf();
            this.pathPoints = new ArrayList<>();
            this.pickupIndex = this.path.calculatePath(shelf.getPosX(), shelf.getPosY(), startIndex, this.pathPoints);

            if (this.pickupIndex == -1) {
                System.err.format("Nemožno doručiť %d kusov tovaru '%s' z dôvodu neexistujúcej cesty.\n", request.getCount(), request.getItemType().getName());
            } else {
                Path.printPath(this.pathPoints);
            }
        }

        @Override
        public void run() {
            return;
        }
    }
}
