/**
 * Súbor obsahuje definíciu triedy Storage.
 * @author Simon Košina
 */

package design.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.min;

/**
 * Trieda reprezentujúca sklad.
 */
public class Storage {
    private ArrayList<Shelf> shelfs = new ArrayList<>();
    private int height = 0;
    private int width = 0;
    private Path path = null;

    /**
     * Vráti hodnotu výšky skladu.
     * @return  výška skladu
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Vráti hodnota šírky skladu.
     * @return šírka skladu
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Pridanie regálu do skladu.
     * @param shelf regál
     */
    public void addShelf(Shelf shelf) {
        if (0 <= shelf.getPosY() && shelf.getPosY() < this.height) {
            if (0 <= shelf.getPosX() && shelf.getPosX() < this.width) {
                this.shelfs.add(shelf);
                return;
            }
        }

        System.err.format("Zadaná pozícia regálu je mimo hranice: [%d, %d]\n", shelf.getPosX(), shelf.getPosY());
    }

    /**
     * Pridanie nového bodu do cesty.
     * @param posX x-ová súrdnica bodu
     * @param posY y-ová súrdnica bodu
     */
    public void addPathPoint(Integer posX, Integer posY) {
        this.path.addPoint(posX, posY);
    }

    /**
     * Pridanie vozíka do objektu cesty.
     * @param maxItems maximálny počet naložených kusov
     */
    public void addCart(int maxItems) {
        this.path.addCart(maxItems);
    }

    /**
     * Vytvorí a pridá do skladu regále na pozíciach ohraníčených bodmi [x1,y1] a [x2, y2], vrátane.
     * @param x1    x-ová súradnica 1. bodu
     * @param y1    y-ová súradnica 1. bodu
     * @param x2    x-ová súradnica 2. bodu
     * @param y2    y-ová súradnica 2. bodu
     */
    public void createShelfs(int x1, int y1, int x2, int y2) {
        if (x2 < x1) {
            int temp;
            temp = x2;
            x2 = x1;
            x1 = temp;
        }

        if (y2 < y1) {
            int temp;
            temp = y2;
            y2 = y1;
            y1 = temp;
        }

        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                Shelf shelf = new Shelf(i,j);
                this.addShelf(shelf);
            }
        }
    }

    /**
     * Parsuje daný súbor a vytvára potrebné objekty skladu.
     * @param filename JSON súbor s popisom celé skladu (regále, položky, cesta)
     * @throws Exception otvorenie a parsovanie súboru
     */
    public void importFile(String filename) throws Exception {
        importShelfs(filename);
        importItems(filename);
        importPath(filename);
        importCarts(filename);
    }

    /**
     * Parsuje uvedený súbor, vytvára a vkladá regále na uvedených pozíciach.
     * @param filename JSON súbor obsahujúci popis regálov
     * @throws Exception otvorenie a parsovanie súboru
     */
    public void importShelfs(String filename) throws Exception {
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONObject jo = (JSONObject) obj;
        JSONArray shelfsArray;

        try {
            this.width = ((Long) jo.get("width")).intValue();
            this.height = ((Long) jo.get("height")).intValue();
            shelfsArray = (JSONArray) jo.get("shelfs");
        } catch (NullPointerException e) {
            // neobsahuje popis regalov
            System.err.format("Sklad nemá špecifikované rozmery alebo pole regálov.\n");
            return;
        }

        if (shelfsArray != null) {
            for (Object o : shelfsArray) {
                Map m = (Map) o;

                int x1 = ((Long) m.get("x1")).intValue();
                int y1 = ((Long) m.get("y1")).intValue();
                int x2 = ((Long) m.get("x2")).intValue();
                int y2 = ((Long) m.get("y2")).intValue();

                this.createShelfs(x1, y1, x2, y2);
            }
        }
    }

    /**
     * Parsuje daný súbor, do regálu na danej pozícii vkladá položky uvedené v súbore.
     * @param filename JSON súbor s popisom vkladaných položiek
     * @throws Exception otvorenie a parsovanie súboru
     */
    public void importItems(String filename) throws Exception {
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONObject jo = (JSONObject) obj;
        JSONArray itemsArray;

        try {
            itemsArray = (JSONArray) jo.get("items");
        } catch (NullPointerException e) {
            // neobsahuje popis poloziek
            System.err.format("Chýba špecifikácia položiek v sklade.\n");
            return;
        }

        if (itemsArray != null) {
            for (Object o : itemsArray) {
                Map m = (Map) o;

                String name = (String) m.get("name");
                Item item = new Item(new ItemType(name));

                int count = ((Long) m.get("count")).intValue();
                int x = ((Long) m.get("x")).intValue();
                int y = ((Long) m.get("y")).intValue();

                Shelf shelf = getShelfByPosition(x, y);

                if (shelf == null) {
                    System.err.format("Nenájdený regál na pozícii [%d, %d]\n", x, y);
                    continue;
                }

                this.getShelfByPosition(x, y).addItem(item, count);
            }
        }
    }

    /**
     * Parsuje daný súbor, načítané body pridáva do cesty v sklade.
     * @param filename JSON súbor s popisom cesty
     * @throws Exception otvorenie a parsovanie súboru
     */
    public void importPath(String filename) throws Exception {
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONObject jo = (JSONObject) obj;

        JSONArray pointsArray;

        try {
            this.path = new Path(((Long) jo.get("startPoint")).intValue());
            pointsArray = (JSONArray) jo.get("path");
        } catch (NullPointerException e) {
            // neobsahuje popis cesty
            System.err.format("Chýba špecifikácia cesty v sklade.\n");
            return;
        }

        if (pointsArray != null) {
            for (Object o : pointsArray) {
                Map m = (Map) o;

                int posX = ((Long) m.get("x")).intValue();
                int posY = ((Long) m.get("y")).intValue();

                this.path.addControlPoint(posX, posY);
            }
        }
    }
    /**
     * Parsuje daný súbor a pridáva uvedené vozíky.
     * @param filename JSON súbor s popisom vozíkov
     * @throws Exception otvorenie a parsovanie súboru
     */
    public void importCarts(String filename) throws Exception {
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONObject jo = (JSONObject) obj;
        JSONArray cartsArray;

        try {
            cartsArray = (JSONArray) jo.get("carts");
        } catch (NullPointerException e) {
            // neobsahuje popis cesty
            System.err.format("Chýba špecifikácia vozíkov v sklade.\n");
            return;
        }

        if (cartsArray != null) {
            for (Object o : cartsArray) {
                Map m = (Map) o;

                int maxItems = ((Long) m.get("maxItems")).intValue();
                this.addCart(maxItems);
            }
        }
    }

    /**
     * Vráti objekt reprezentujúci regál na uvedenej pozícii.
     * @param x pozícia na x-ovej súradnici
     * @param y pozícia na y-ovej súradnici
     * @return  regál umiestnený na špecifikovanej pozícii
     */
    public Shelf getShelfByPosition(int x, int y) {
        for (Shelf s : this.shelfs) {
            if (s.getPosX() == x && s.getPosY() == y) {
                return s;
            }
        }

        return null;
    }

    /**
     * Vráti zoznam všetkých regálov.
     * @return  zoznam regálov
     */
    public ArrayList<Shelf> getAllShelfs(){
        return shelfs;
    }

    /**
     * Vypísanie informácií o sklade na štandardný výstup. Slúži na testovanie.
     */
    public void printStorage() {
//        for (Shelf s : this.shelfs) {
//            s.printShelf();
//        }

        this.addRequest("vaňa", 1);
        this.addRequest("polička", 2);

        //this.addRequest("bvrrrrrm", 2);

    }


    /**
     * Pridanie položky do aktuálneho požiadavku.
     * @param itemName názov položky
     * @param count počet vyžadovaných kusov
     */
    public void addRequest(String itemName, int count) {
        ItemType itemType = new ItemType(itemName);
        int index = 0;
        int foundCount = 0;

        while (count > 0) {
            index = this.findItems(itemType, index);

            if (index == -1) {
                System.err.format("Nemožno kompletne vybaviť požiadavok z dôvodu nedostatku tovaru. ");
                System.err.format("Nebude doručených %d položiek typu '%s'.\n", count, itemType.getName());
                break;
            }

            foundCount = this.shelfs.get(index).countItems(itemType);

            this.path.addRequest(new Request(this.shelfs.get(index), min(foundCount, count), itemType));

            count -= foundCount;
            index += 1;
        }
    }

    /**
     * Vráti index regálu v zozname shelfs, ktorý obsahuje danú položku.
     * @return index nájdeného regálu
     */
    public int findItems(ItemType itemType, int startIndex) {
        for (int i = startIndex; i < shelfs.size(); i++) {
            if (this.shelfs.get(i).countItems(itemType) > 0) {
                return i;
            }
        }

        return -1;
    }

    public Path getPath(){
        return this.path;
    }
}
