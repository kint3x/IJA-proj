package design.model;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Path {
    private ArrayList<PathPoint> points = new ArrayList<>();
    private ArrayList<Cart> carts = new ArrayList<>();

    /**
     * Pridanie nového bodu do cesty. Daný bod musí byť susedný s konečnými bodmi cesty.
     * @param posX x-ová pozícia bodu
     * @param posY y-ová pozícia bodu
     */
    public void addPoint(Integer posX, Integer posY) {
        Boolean correct = false;
        PathPoint newPoint = new PathPoint(posX, posY);

        if (this.points.size() == 0) {
            correct = true;
        } else {
            // musi navazovat bud na koncovy alebo pociatocny bod
            correct = isContinuous(this.points.get(this.points.size() - 1), newPoint);
            correct = correct || isContinuous(this.points.get(0), newPoint);

            // nesmie sa vratit na uz existujuci bod
            for (PathPoint point : this.points) {
                correct = correct && isDifferent(point, newPoint);
            }
        }

        if (!correct) {
            System.err.format("Neplatný bod cesty: [%d, %d]\n", posX, posY);
            return;
        }

        this.points.add(newPoint);
    }

    /**
     * Metóda nájde bod cesty na daných súradniciach, pomocou metódy binárneho hľadania.
     * @param posX x-ová súradnica hľadaného bodu
     * @param posY y-ová súradnica hľadaného bodu
     * @return hľadaný bod alebo null, v prípade, že daný bod neexistuje
     */
    public PathPoint findPoint(Integer posX, Integer posY) {
        Integer l = 0;
        Integer r = this.points.size() - 1;

        while (l<=r) {
            Integer lDistance, rDistance;
            Integer m = l + (r - l) / 2;
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
     * Vypočíta Manhattanovskú vzdialenosť medzi bodmi [x1, y1] a [x2, y2].
     * @param x1 x-ová súrdanica prvého bodu
     * @param y1 y-ová súrdanica prvého bodu
     * @param x2 x-ová súrdanica druhého bodu
     * @param y2 y-ová súrdanica druhého bodu
     */
    public Integer distance(Integer x1, Integer y1, Integer x2, Integer y2) {
        return abs(x1 - x2) + abs(y1 - y2);
    }

    /**
     * Overí, či dané 2 body sú spojité.
     * @param point1 prvý bod
     * @param point2 druhý bod
     * @return true, ak sú spojité, inak false
     */
    private Boolean isContinuous(PathPoint point1, PathPoint point2) {
        Integer dx = point1.getPosX() - point2.getPosX();
        Integer dy = point1.getPosY() - point2.getPosY();

        // posunutie o 1 bud v ramci x alebo y osi
        if (dx == 0 && (dy == 1 || dy == -1)) {
            return true;
        } else if (dy == 0 && (dx == 1 || dx == -1)) {
            return true;
        }

        return false;
    }

    /**
     * Overí, či dané 2 body nie sú rovnaké (majú rovnakú pozíciu).
     * @param point1 prvý bod
     * @param point2 druhý bod
     * @return true, ak dané 2 body sú rozdielne, inak false
     */
    private Boolean isDifferent(PathPoint point1, PathPoint point2) {
        if (point1.getPosX() == point2.getPosX()) {
            if (point1.getPosY() == point2.getPosY()) {
                return false;
            }
        }

        return true;
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
}
