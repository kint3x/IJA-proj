package design.model;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.abs;
import static java.lang.Math.random;

public class Path {
    private int changeCounter = 0;
    private final Object changeCounterLock = new Object();
    private final ArrayList<PathPoint> points = new ArrayList<>();
    private final Object pointsLock = new Object();
    private final ArrayList<Cart> carts = new ArrayList<>();
    private final int dropIndex;
    private int prevX = -1;
    private int prevY = -1;
    private ArrayList<Request> waitingRequests = new ArrayList<>();
    private ArrayList<Request> openRequests = new ArrayList<>();

    /**
     * Konštruktor.
     * @param dropIndex počiatočný index cesty pre vozíky
     */
    public Path(int dropIndex) {
        this.dropIndex = dropIndex;
    }

    /**
     * Vráti hodnotu čítača zmien cesty.
     * @return hodnota čítača
     */
    public int getChangeCounter() {
        synchronized (changeCounterLock) {
            return changeCounter;
        }
    }

    public int getDropIndex() {
        return this.dropIndex;
    }

    public ArrayList<Request> getWaitingRequests() {
        return this.waitingRequests;
    }

    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }

    /**
     * Inkrementuje hodnotu čítača zmien.
     */
    public void incrementCounter() {
        synchronized (changeCounterLock) {
            this.changeCounter += 1;
        }
    }

    /**
     * Pridanie nového bodu do cesty. Daný bod musí byť susedný s konečnými bodmi cesty.
     * @param posX x-ová pozícia bodu
     * @param posY y-ová pozícia bodu
     */
    public void addPoint(int posX, int posY) {
        synchronized (pointsLock) {
            boolean correct = false;
            PathPoint newPoint = new PathPoint(posX, posY);

            if (this.getPoints().size() == 0) {
                correct = true;
            } else {
                // musi navazovat bud na koncovy alebo pociatocny bod
                correct = isContinuous(this.getPoints().get(this.getPoints().size() - 1), newPoint);
                correct = correct || isContinuous(this.getPoints().get(0), newPoint);

                // nesmie sa vratit na uz existujuci bod
                for (PathPoint point : this.getPoints()) {
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

            this.getPoints().add(newPoint);
        }

        this.incrementCounter();
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
        synchronized (pointsLock) {
            PathPoint point = findPoint(posX, posY);

            if (point != null) {
                point.switchBlocked();
            }
        }

        this.incrementCounter();
    }

    /**
     * Metóda nájde bod cesty na daných súradniciach.
     * @param posX x-ová súradnica hľadaného bodu
     * @param posY y-ová súradnica hľadaného bodu
     * @return hľadaný bod alebo null, v prípade, že daný bod neexistuje
     */
    public PathPoint findPoint(int posX, int posY) {
        synchronized (pointsLock) {
                PathPoint goal = new PathPoint(posX, posY);

                for (PathPoint p: this.getPoints()) {
                    if (p.equals(goal)) {
                        return p;
                    }
            }

            return null;
        }
    }

    /**
     * Metóda nájde bod cesty na daných súradniciach, pomocou metódy binárneho hľadania.
     * @param posX x-ová súradnica hľadaného bodu
     * @param posY y-ová súradnica hľadaného bodu
     * @return index hľadaného bodu, alebo -1, ak sa bod nenašiel
     */
    public int findPointIndex(int posX, int posY) {
        synchronized (pointsLock) {
            PathPoint goal = new PathPoint(posX, posY);

            for (int i = 0; i < this.getPoints().size(); i++) {
                if (this.getPoints().get(i).equals(goal)) {
                    return i;
                }
            }

            return -1;
        }
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
     * Vráti index najbližšieho prístupového bodu cesty s indexom pIndex k bodu [posX, posY].
     * @param pIndex index bodu cesty
     * @param posX x-ová súraanica bodu
     * @param posY y-ová súradnica bodu
     * @return index najbližšieho bodu
     */
    public int findClosestPointIndex(int pIndex, int posX, int posY) {
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
                if((abs(resIndexes[i] - pIndex) < abs(minIndex - pIndex)) || minIndex == -1) {
                    minIndex = resIndexes[i];
                }
            }
        }

        return minIndex;
    }

    /**
     * Vypočíta cestu k poličke.
     * @param posX x-ová pozícia regálu
     * @param posY y-ová pozícia regálu
     * @oaram cartX x-ová pozícia vozíka na ceste
     * @param cartY y-ová pozícia vozíka na ceste
     * @param path zoznam bodov, ktorý bude modifikovaný, nesmie byť null
     * @return index v ceste, kde je nutné vyzdvihnúť náklad
     */
    public int calculatePath2Shelf(int posX, int posY, int cartX, int cartY, ArrayList<PathPoint> path) {
        synchronized (pointsLock) {
            if (path == null) {
                return -1;
            }

            int cartIndex = this.findPointIndex(cartX, cartY);
            int minIndex = this.findClosestPointIndex(cartIndex, posX, posY);

            if (minIndex == -1) {
                return minIndex;
            }

            // Vytvor cestu
            ArrayList<PathPoint> path2Shelf;

            path2Shelf = createPathBetweenPoints(cartIndex, minIndex);

            if (path2Shelf == null) {
                return -1;
            }

            path.addAll(path2Shelf);

            return path.size() - 1;
        }
    }

    /**
     * Vypočíta cestu k miestu pre vyzdvihnutie tovaru.
     * @oaram cartX x-ová pozícia vozíka na ceste
     * @param cartY y-ová pozícia vozíka na ceste
     * @param path zoznam bodov, ktorý bude modifikovaný, nesmie byť null
     * @return index v ceste, kde je nutné vyzdvihnúť náklad
     */
    public int calculatePath2Drop(int cartX, int cartY, ArrayList<PathPoint> path) {
        synchronized (pointsLock) {
            if (path == null) {
                return -1;
            }

            int cartIndex = this.findPointIndex(cartX, cartY);
            int endIndex = this.dropIndex;

            // Vytvor cestu
            ArrayList<PathPoint> path2Drop;

            path2Drop = createPathBetweenPoints(cartIndex, endIndex);

            if (path2Drop == null) {
                return -1;
            }

            path.addAll(path2Drop);

            return path.size() - 1;
        }
    }

    /**
     * Vytvorenie cesty medzi 2 bodmi, vrátane daných bodov
     * @param index1 index prvého bodu
     * @param index2 index druhého bodu
     * @return zoznam bodov v ceste
     */
    public ArrayList<PathPoint> createPathBetweenPoints(int index1, int index2) {
        synchronized (this.pointsLock) {
            int dist1, dist2;
            boolean blocked = false;
            ArrayList<PathPoint> path = new ArrayList<>();

            int diff = index1 < index2 ? 1 : -1;

            if (this.pathConnected()) {
                // vzdialenost ak ide priamo
                dist1 = abs(index2 - index1);
                // vzdialenost ak prejde cez start
                dist2 = index1 < index2 ? index1 + this.getPoints().size() - index2 : this.getPoints().size() - index1 + index2;

                if (dist2 < dist1) {
                    diff *= -1;
                }
            }

            int stop = ((index2 + diff) < 0 ? this.getPoints().size() + (index2 + diff) : (index2 + diff)) % this.getPoints().size();

            // Cyklus cez zoznam bodov, ak je spojeny je mozne ist aj dozadu
            for (int i = index1; (i < 0 ? this.getPoints().size() + i : i) % this.getPoints().size() != stop; i += diff) {
                PathPoint p = this.getPoints().get((i < 0 ? this.getPoints().size() + i : i) % this.getPoints().size());
                if (p.isBlocked()) {
                    blocked = true;
                    break;
                }
                path.add(p);
            }

            // Zablokovana cesta, skusime opacny smer
            if (blocked) {
                if (this.pathConnected()) {
                    path = new ArrayList<>();
                    diff *= -1;

                    stop = ((index2 + diff) < 0 ? this.getPoints().size() + (index2 + diff) : (index2 + diff)) % this.getPoints().size();

                    for (int i = index1; (i < 0 ? this.getPoints().size() + i : i) % this.getPoints().size() != stop; i += diff) {
                        PathPoint p = this.getPoints().get((i < 0 ? this.getPoints().size() + i : i) % this.getPoints().size());
                        if (p.isBlocked()) {
                            return null;
                        }
                        path.add(p);
                    }

                } else {
                    return null;
                }
            }

            return path;
        }
    }

    /**
     * Zistí, či je cesta spojená, t.j. začiatočný a koncový bod susedia.
     * @return true, ak je cesta spojená, inak false
     */
    public boolean pathConnected() {
        synchronized (pointsLock) {
            if (this.getPoints().size() > 1) {
                PathPoint start = this.getPoints().get(0);
                PathPoint end = this.getPoints().get(this.getPoints().size() - 1);

                return (abs(start.getPosX() - end.getPosX()) + abs(start.getPosY() - end.getPosY()) == 1);
            }

            return false;
        }
    }

    /**
     * Pridá požiadavok na zoznam nevybavených požiadavkov.
     * @param request požiadavok
     */
    public void addRequest(Request request) {
        request.getShelf().removeItem(request.getItemType(), request.getCount());
        this.getOpenRequests().add(request);
    }

    /**
     * Nevybavené požiadavok pridá na zoznam spracovývaných požiadavkov.
     */
    public void processRequests() {
        this.waitingRequests.addAll(this.getOpenRequests());
        this.openRequests = new ArrayList<>();

        this.deliverRequests();
    }

    public void deliverRequests() {

        while (this.getWaitingRequests().size() > 0) {
            int freeCarts = 0;
            boolean added = false;

            // pridaj objednavku najmensiemu moznemu voziku
            for (Cart c : this.getCarts()) {
                if (!c.getBusy()) {
                    freeCarts += 1;

                    if (c.getMaxItems() >= this.getWaitingRequests().get(0).getCount()) {
                        // pridaj objednavku voziku a odober zo zoznamu
                        c.addRequest(this.getWaitingRequests().get(0));
                        this.getWaitingRequests().remove(0);
                        added = true;

                        // skusi pridat co najviac objednavok
                        while (this.getWaitingRequests().size() > 0) {
                            if (c.getMaxItems() - c.getRequestsItemsCount() >= this.getWaitingRequests().get(0).getCount()) {
                                c.addRequest(this.getWaitingRequests().get(0));
                                this.getWaitingRequests().remove(0);
                            } else {
                                break;
                            }

                        }

                        c.deliverRequests();
                        break;
                    }
                }
            }

            if (added) {
                continue;
            }

            // ziaden volny vozik
            if (freeCarts == 0) {
                break;
            }

            // prida objednavku nahodnemu voziku zo zoznamu
            this.getCarts().get((int) (random()*(this.getCarts().size()-1)));
            this.getWaitingRequests().remove(this.getWaitingRequests().get(0));
        }
    }

    /**
     * Vypíše cestu v danom objekte triedy Path.
     */
    public void printPath() {
        Path.printPath(this.getPoints());
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
        Cart cart = new Cart(dropIndex, maxItems, this);
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
        synchronized (pointsLock) {
            return this.points;
        }
    }

    public ArrayList<Cart> getCarts(){
        return this.carts;
    }

}
