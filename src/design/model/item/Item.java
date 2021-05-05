/**
 * Súbor obsahuje definíciu triedy Item.
 * @author Simon Košina
 */

package design.model.item;

/**
 * Trieda reprezentujúca položku.
 */
public class Item {
    private ItemType type;

    /**
     * Konštruktor.
     * @param type  typ položky
     */
    public Item(ItemType type) {
        this.type = type;
    }

    /**
     * Zistenie typu položky.
     * @return  typ položky
     */
    public ItemType getType() {
        return this.type;
    }
}
