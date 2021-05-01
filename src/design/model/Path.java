package design.model;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Path {
    private ArrayList<PathPoint> points = new ArrayList<>();
    private ArrayList<Cart> carts = new ArrayList<>();
    private int cartsIndex;

    /**
     * Konštruktor.
     * @param cartsIndex počiatočný index cesty pre vozíky
     */
    public Path(int cartsIndex) {
        this.cartsIndex = cartsIndex;
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
                correct = correct && !point.equals(newPoint);
            }
        }

        if (!correct) {
            System.err.format("Neplatný bod cesty: [%d, %d]\n", posX, posY);
            return;
        }

        this.points.add(newPoint);
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
            lDistance = this.distance(posX, posY, this.points.get(l).getPosX(), this.points.get(l).getPosY());
            rDistance = this.distance(posX, posY, this.points.get(r).getPosX(), this.points.get(r).getPosY());

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
            lDistance = this.distance(posX, posY, this.points.get(l).getPosX(), this.points.get(l).getPosY());
            rDistance = this.distance(posX, posY, this.points.get(r).getPosX(), this.points.get(r).getPosY());

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
     * Vráti index bodu na ceste vzdialený o 1 od danej pozície.
     * @param posX x-ová súradnica
     * @param posY y-ová súradnica
     * @return index daného bodu v pathPoints alebo -1
     */
    public int getClosestPoint(int posX, int posY) {
        int[] dx = {+1, -1, +0, +0};
        int[] dy = {+0, +0, -1, +1};
        int res;

        for (int i = 0; i < 4; i++) {
            res = this.findPointIndex(posX + dx[i], posY + dy[i]);

            if (res != -1) {
                return res;
            }
        }

        return -1;
    }

    /**
     * Doručenie položiek.
     * @param request požiadavka
     */
    public void deliverRequest(Request request) {

    }

    /**
     * Vypíše jednotlivé body cesty. Slúži pre testovanie.
     */
    public void printPath() {
        if (this.points != null) {
            System.out.format("Cesta:\n");

            for (PathPoint p : this.points) {
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
        Cart cart = new Cart(cartsIndex, maxItems);
        this.carts.add(cart);
    }

    public ArrayList<PathPoint> getPoints(){
        return this.points;
    }
}
