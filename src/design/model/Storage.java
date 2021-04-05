package design.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Trieda reprezentujúca sklad.
 */
public class Storage {
    private ArrayList<Shelf> shelfs = new ArrayList<>();

    /**
     * Pridanie regálu do skladu.
     * @param shelf regál
     */
    public void addShelf(Shelf shelf) {
        shelfs.add(shelf);
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
     * Parsuje uvedený súbor, vytvára a vkladá regále na uvedených pozíciach.
     * @param filename JSON súbor obsahujúci popis regálov
     * @throws Exception otvorenie a parsovanie súboru
     */
    public void importShelfs(String filename) throws Exception {
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONObject jo = (JSONObject) obj;
        JSONArray ja = (JSONArray) jo.get("shelfs");

        for (Object o : ja) {
            Map m = (Map) o;

            int x1 = ((Long) m.get("x1")).intValue();
            int y1 = ((Long) m.get("y1")).intValue();
            int x2 = ((Long) m.get("x2")).intValue();
            int y2 = ((Long) m.get("y2")).intValue();

            this.createShelfs(x1, y1, x2, y2);
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
        JSONArray ja = (JSONArray) jo.get("items");

        for (Object o : ja) {
            Map m = (Map) o;

            String name = (String) m.get("name");
            Item item = new Item(new ItemType(name));

            int count = ((Long) m.get("count")).intValue();
            int x = ((Long) m.get("x")).intValue();
            int y = ((Long) m.get("y")).intValue();

            Shelf shelf = getShelfByPosition(x,y);

            if (shelf == null) {
                System.err.format("Nenájdený regál na pozícii x: %d, y:%d\n", x, y);
            }

            this.getShelfByPosition(x,y).addItem(item, count);
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

    public ArrayList<Shelf> getAllShelfs(){
        return shelfs;
    }

    public void printStorage() {
        for (Shelf s : this.shelfs) {
            s.printShelf();
        }
    }
}
