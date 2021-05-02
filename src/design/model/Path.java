package design.model;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.abs;

public class Path {
    private final ArrayList<PathPoint> points = new ArrayList<>();
    private final ArrayList<Cart> carts = new ArrayList<>();
    private final int dropIndex;
    private int prevX = -1;
    private int prevY = -1;

    /**
     * Konštruktor.
     * @param dropIndex počiatočný index cesty pre vozíky
     */
    public Path(int dropIndex) {
        this.dropIndex = dropIndex;
    }

    /**
     * Pridanie nového bodu do cesty. Daný bod musí byť susedný s konečnými bodmi cesty.
     * @param posX x-ová pozícia bodu
     * @param posY y-ová pozícia bodu
     */
    public void addPoint(int posX, int posY) {
        boolean correct = false;
        PathPoint newPoint = new PathPoint(posX, posY);

        if (this.points.size() == 0) {
            correct = true;
        } else {
            // musi navazovat bud na koncovy alebo pociatocny bod
            correct = isContinuous(this.points.get(this.points.size() - 1), newPoint);
            correct = correct || isContinuous(this.points.get(0), newPoint);

            // nesmie sa vratit na uz existujuci bod
            for (PathPoint point : this.points) {
                // preskocenie uz existujuich bodov
                if (point.equals(newPoint)) {
                    return;
                }
            }
        }

        if (!correct) {
            System.err.format("Neplatný bod cesty: [%d, %d]\n", posX, posY);
            return;
        }

        this.points.add(newPoint);
    }

    /**
     * Postupne pridavanie bodov a vytváranie cesty medzi nimi.
     * @param posX x-ová súradnica bodu
     * @param posY y-ová súradnica bodu
     */
    public void addControlPoint(int posX, int posY) {
        if (this.prevX == -1 && this.prevY == -1) {
            this.prevX = posX;
            this.prevY = posY;
        }

        // zmena mozna iba v jednom smere
        if (prevX != posX && prevY != posY) {
            System.err.format("Chyba pri načítaní cesty, body musia mať spoločnú minimálne jednu súradnicu.\n");
            return;
        }

        int dx = this.prevX < posX ? 1 : -1;
        int dy = this.prevY < posY ? 1 : -1;

        for (int x = this.prevX; x != posX + dx; x += dx) {
            addPoint(x, this.prevY);
        }

        for (int y = this.prevY; y != posY + dy; y += dy) {
            addPoint(this.prevX, y);
        }

        this.prevX = posX;
        this.prevY = posY;
    }

    /**
     * Zablokuje alebo odblokuje cestu na danej pozícii.
     * @param posX x-ová súradnica bodu
     * @param posY y-ová súradnica bodu
     */
    public void blockPoint(int posX, int posY) {
        PathPoint point = findPoint(posX, posY);

        if (point != null) {
            point.switchBlocked();
        }
    }

    /**
     * Metóda nájde bod cesty na daných súradniciach, pomocou metódy binárneho hľadania.
     * @param posX x-ová súradnica hľadaného bodu
     * @param posY y-ová súradnica hľadaného bodu
     * @return hľadaný bod alebo null, v prípade, že daný bod neexistuje
     */
    public PathPoint findPoint(int posX, int posY) {
        int l = 0;
        int r = this.points.size() - 1;

        while (l<=r) {
            int lDistance, rDistance;
            int m = l + (r - l) / 2;
            PathPoint mPoint = this.points.get(m);

            // nasiel sa dany bod
            if (mPoint.getPosX() == posX && mPoint.getPosY() == posY) {
                return mPoint;
            }

            // vypocet Manhattatnovskych vzdialenost od laveho a praveho konca intervalu
            lDistance = distance(posX, posY, this.points.get(l).getPosX(), this.points.get(l).getPosY());
            rDistance = distance(posX, posY, this.points.get(r).getPosX(), this.points.get(r).getPosY());

            if (lDistance < rDistance) {
                // lava polovica
                r = m - 1;
            } else {
                // prava polovica
                l = m + 1;
            }
        }

        return null;
    }

    /**
     * Metóda nájde bod cesty na daných súradniciach, pomocou metódy binárneho hľadania.
     * @param posX x-ová súradnica hľadaného bodu
     * @param posY y-ová súradnica hľadaného bodu
     * @return index hľadaného bodu, alebo -1, ak sa bod nenašiel
     */
    public int findPointIndex(int posX, int posY) {
        int l = 0;
        int r = this.points.size() - 1;

        while (l<=r) {
            int lDistance, rDistance;
            int m = l + (r - l) / 2;
            PathPoint mPoint = this.points.get(m);

            // nasiel sa dany bod
            if (mPoint.getPosX() == posX && mPoint.getPosY() == posY) {
                return m;
            }

            // vypocet Manhattatnovskych vzdialenost od laveho a praveho konca intervalu
            lDistance = distance(posX, posY, this.points.get(l).getPosX(), this.points.get(l).getPosY());
            rDistance = distance(posX, posY, this.points.get(r).getPosX(), this.points.get(r).getPosY());

            if (lDistance < rDistance) {
                // lava polovica
                r = m - 1;
            } else {
                // prava polovica
                l = m + 1;
            }
        }

        return -1;
    }

    /**
     * Vypočíta Manhattanovskú vzdialenosť medzi bodmi [x1, y1] a [x2, y2].
     * @param x1 x-ová súrdanica prvého bodu
     * @param y1 y-ová súrdanica prvého bodu
     * @param x2 x-ová súrdanica druhého bodu
     * @param y2 y-ová súrdanica druhého bodu
     */
    public static int distance(int x1, int y1, int x2, int y2) {
        return abs(x1 - x2) + abs(y1 - y2);
    }

    /**
     * Overí, či dané 2 body sú spojité.
     * @param point1 prvý bod
     * @param point2 druhý bod
     * @return true, ak sú spojité, inak false
     */
    private boolean isContinuous(PathPoint point1, PathPoint point2) {
        int dx = point1.getPosX() - point2.getPosX();
        int dy = point1.getPosY() - point2.getPosY();

        // posunutie o 1 v ramci x alebo y osi
        if (dx == 0 && (dy == 1 || dy == -1)) {
            return true;
        } else if (dy == 0 && (dx == 1 || dx == -1)) {
            return true;
        }

        return false;
    }

    /**
     * Vypočíta cestu k poličke a naspäť.
     * @param posX x-ová pozícia regálu
     * @param posY y-ová pozícia regálu
     * @param start index do points (pozicia vozíka)
     * @param path zoznam bodov, ktorý bude modifikovaný, nesmie byť null
     * @return index v ceste, kde je nutné vyzdvihnúť náklad
     */
    public int calculatePath(int posX, int posY, int start, ArrayList<PathPoint> path) {
        if (path == null) {
            return -1;
        }

        int minIndex = -1;
        int[] resIndexes = {-1, -1, -1, -1};
        int[] dx = {+1, -1, +0, +0};
        int[] dy = {+0, +0, -1, +1};

        // Najde mnozinu moznych pristupovych bodov k regalu
        for (int i = 0; i < 4; i++) {
            resIndexes[i] = this.findPointIndex(posX + dx[i], posY + dy[i]);
        }

        // Najde minimum z danej mnoziny
        for (int i = 0; i < 4; i++) {
            if (resIndexes[i] != -1) {
                if (this.pathBlocked(start, resIndexes[i])) {
                    // cesta je zablokovana
                    resIndexes[i] = -1;
                } else {
                    // cesta je volna, najdi najblizsi bod
                    if ((abs(resIndexes[i] - start) < abs(minIndex - start)) || minIndex == -1) {
                        minIndex = resIndexes[i];
                    }
                }
            }
        }

        if (minIndex == -1) {
            return minIndex;
        }

        // Vytvor cestu
        ArrayList<PathPoint> path2Shelf, path2Drop;

        path2Shelf = createPathBetweenPoints(start, minIndex);
        path2Drop = createPathBetweenPoints(minIndex, this.dropIndex);

        path.addAll(path2Shelf);
        path.remove(path.size()-1);
        path.addAll(path2Drop);

        return minIndex;
    }

    /**
     * Vytvorenie cesty medzi 2 bodmi, vrátane daných bodov
     * @param index1 index prvého bodu
     * @param index2 index druhého bodu
     * @return zoznam bodov v ceste
     */
    public ArrayList<PathPoint> createPathBetweenPoints(int index1, int index2) {
        int dist1, dist2;
        ArrayList<PathPoint> path = new ArrayList<>();

        int diff = index1 < index2 ? 1 : -1;

        if (this.pathConnected()) {
            // vzdialenost ak ide priamo
            dist1 = abs(index2 - index1);
            // vzdialenost ak prejde cez start
            dist2 = index1 < index2 ? index1 + this.points.size() - index2 : this.points.size() - index1 + index2;

            if (dist2 < dist1) {
                diff *= -1;
            }
        }

        for (int i = index1; i != index2 + diff; i += diff) {
            path.add(this.points.get(abs(i) % this.points.size()));
        }

        return path;
    }

    /**
     * Zistí či daný usek cesty je zablokovaný.
     * @param start počiatočný index úseku
     * @param end konečný index úseku
     * @return true, ak daný úsek cesty je zablokovaný, inak false
     */
    public boolean pathBlocked(int start, int end) {
        int diff = start < end ? 1 : -1;

        for (int i = start; i != end + diff; i += diff) {
            if (this.points.get(i).isBlocked()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Zistí, či je cesta spojená, t.j. začiatočný a koncový bod susedia.
     * @return true, ak je cesta spojená, inak false
     */
    public boolean pathConnected() {
        if (this.points.size() > 1) {
            PathPoint start = this.points.get(0);
            PathPoint end = this.points.get(this.points.size() - 1);

            return (abs(start.getPosX() - end.getPosX()) + abs(start.getPosY() - end.getPosY()) == 1);
        }

        return false;
    }

    /**
     * Doručenie položiek. Vozíku pridá požiadavok
     * @param request požiadavka
     */
    public void addRequest(Request request) {
        for (Cart cart: this.carts) {
            if (cart.getMaxItems() >= request.getCount() && !cart.getBusy()) {
                cart.deliverRequest(request, this);
                break;
            }
        }
    }

    /**
     * Vypíše cestu v danom objekte triedy Path.
     */
    public void printPath() {
        Path.printPath(this.points);
    }

    /**
     * Vypíše jednotlivé body cesty. Slúži pre testovanie.
     * @param points zoznam bodov cesty
     */
    public static void printPath(ArrayList<PathPoint> points) {
        if (points != null) {
            System.out.format("Cesta:\n");

            for (PathPoint p : points) {
                System.out.format("\t");
                p.printPoint();
            }
        }
    }

    /**
     * Pridanie vozíka.
     * @param maxItems maximálny počet naložených kusov
     */
    public void addCart(int maxItems) {
        Cart cart = new Cart(dropIndex, maxItems);
        this.carts.add(cart);

        // vzostupne zoradenie vozikov podla nosnosti
        this.carts.sort(new Comparator<Cart>() {
            @Override
            public int compare(Cart o1, Cart o2) {
                return o1.getMaxItems() - o2.getMaxItems();
            }
        });
    }

    public ArrayList<PathPoint> getPoints(){
        return this.points;
    }
}
