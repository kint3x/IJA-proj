package design.model;

import java.util.Objects;

public class ItemType {
    private String name;

    public ItemType(String name) {
        this.name = name;
    }

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
