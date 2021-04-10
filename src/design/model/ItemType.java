/**
 * Súbor obsahuje definíciu triedy ItemType.
 * @author Simon Košina
 */

package design.model;

import java.util.Objects;

/**
 * Trieda reprezentujúca typ položky.
 */
public class ItemType {
    private String name;

    /**
     * Konštruktor.
     * @param name  názov
     */
    public ItemType(String name) {
        this.name = name;
    }

    /**
     * Zistí názov položky
     * @return  názov položky
     */
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemType itemType = (ItemType) o;
        return name.equals(itemType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
