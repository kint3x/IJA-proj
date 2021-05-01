package design.model;

/**
 * Trieda predstavuje položku na zozname požiadavkov.
 *
 */
public class RequestItem {
    /**
     * Regál, z ktorého bude prevzatá položka.
     */
    private Shelf shelf;

    /**
     * Počet odobratých položiek z daného regálu.
     */
    private int count;

    /**
     * Typ odoberanej položky.
     */
    private ItemType itemType;

    /**
     * Index bodu na ceste, na ktorý sa musí dostaviť vozík.
     */
    private int pointIndex;

    /**
     * Konštruktor
     * @param shelf regál pre odobratie položky
     * @param count počet odobratých položiek
     * @param itemType typ odobraných položiek
     * @param pointIndex index bodu v ceste
     */
    public RequestItem(Shelf shelf, int count, ItemType itemType, int pointIndex) {
        this.shelf = shelf;
        this.count = count;
        this.itemType = itemType;
        this.pointIndex = pointIndex;
    }

    /**
     * Vráti index do pola bodov v ceste.
     * @return index
     */
    public int getPointIndex() {
        return this.pointIndex;
    }

    /**
     * Výpis objektu. Slúži najmä pre testovanie.
     */
    public void printRequestItem() {
        System.out.format("regal: [%d, %d], index bodu: %d, polozka: '%s', pocet: %d\n",
                shelf.getPosX(), shelf.getPosY(), this.pointIndex, this.itemType.getName(), this.count);
    }
}
