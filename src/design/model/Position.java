package design.model;

/**
 * Trieda reprezentujúca polohu v 2D priestore.
 */
public class Position {
    private int posX;
    private int posY;

    /**
     * Konštruktor.
     * @param posX x-ová súradnica
     * @param posY y-ová súradnica
     */
    public Position (int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Vráti polohu na y-ovej súradnici.
     * @return  x-ová súradnica
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Vráti polohu na x-ovej súradnici.
     * @return  y-ová súradnica
     */
    public int getPosY() {
        return posY;
    }
}
