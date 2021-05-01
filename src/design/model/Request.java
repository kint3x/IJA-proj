package design.model;

import java.util.ArrayList;
import java.util.Comparator;

public class Request {
    private ArrayList<RequestItem> requestItems = new ArrayList<>();
    private int cost = 0;

    /**
     * Pridanie položky do zoznamu.
     * @param shelf regál, kde sa položka nachádza
     * @param count počet položiek
     * @param itemType typ položky
     * @param pointIndex index bodu na ceste, na ktorý sa musí dostaviť vozík
     */
    public void addRequestItem(Shelf shelf, int count, ItemType itemType, int pointIndex) {
        this.requestItems.add(new RequestItem(shelf, count, itemType, pointIndex));
        this.increaseCost(count);
    }

    /**
     * Zvýšenie ceny (počtu položiek).
     * @param n počet
     */
    public void increaseCost(int n) {
        this.cost += n;
    }

    /**
     * Zníženie ceny (počtu položiek).
     * @param n počet
     */
    public void decreaseCost(int n) {
        this.cost -= n;
    }

    /**
     * Zoradí položky podľa indexu do pola bodov cesty.
     */
    public void orderRequestItems() {
        requestItems.sort(new Comparator<RequestItem>() {
            @Override
            public int compare(RequestItem o1, RequestItem o2) {
                return o1.getPointIndex() - o2.getPointIndex();
            }
        });
    }

    /**
     * Výpis požiadavku. Slúži najmä pre testovanie.
     */
    public void printRequest() {
        if (this.requestItems != null) {
            for (RequestItem requestItem : this.requestItems) {
                requestItem.printRequestItem();
            }
        }
    }
}
