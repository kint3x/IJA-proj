package design.model;

import java.util.ArrayList;

public class Path {
    private ArrayList<PathPoint> points;
    private ArrayList<Cart> carts;

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
}
