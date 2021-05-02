package design.model;

/**
 * Trieda predstavuje položku na zozname požiadavkov.
 *
 */
public class Request {
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
     * Konštruktor
     * @param shelf regál pre odobratie položky
     * @param count počet odobratých položiek
     * @param itemType typ odobraných položiek
     */
    public Request(Shelf shelf, int count, ItemType itemType) {
        this.shelf = shelf;
        this.count = count;
        this.itemType = itemType;
    }

    /**
     * Získa počet položiek, ktoré treba doručiť
     * @return počet položiek
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Získa cieľový regál.
     * @return regál
     */
    public Shelf getShelf() {
        return this.shelf;
    }

    /**
     * Získa typ položiek.
     * @return typ položiek
     */
    public ItemType getItemType() {
        return this.itemType;
    }

    /**
     * Výpis objektu. Slúži najmä pre testovanie.
     */
    public void printRequestItem() {
        System.out.format("regal: [%d, %d], polozka: '%s', pocet: %d\n",
                shelf.getPosX(), shelf.getPosY(), this.itemType.getName(), this.count);
    }
}
