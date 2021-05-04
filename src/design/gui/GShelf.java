/**
 * Súbor obsahuje definíciu triedy GEmpty.
 * @author Martin Matějka
 */

package design.gui;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import design.controllers.StorageController;
import design.model.Shelf;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.awt.*;

/**
 * Trieda, pre uchovanie grafickej podoby regálu.
 */
public class GShelf {
    private Shelf shelf;
    private Rectangle rect;
    private StorageController controller;

    /**
     * Pri inicializácií sa zapíše inštancia kontroleru a inštancia regálu ktorý táto trieda predstavuje.
     * @param s inštancia regálu
     * @param c inštancia kontroleru
     */
    public GShelf(Shelf s, StorageController c){

        controller=c;
        shelf = s;
        rect = new Rectangle();
        rectDefaultstyle();
    }

    /**
     * Nastaví východzí štýl políčka s regálom.
     */
    private void rectDefaultstyle(){
        rect.setHeight(controller.getRect_s());
        rect.setWidth(controller.getRect_s());
        rect.setStyle("-fx-fill: aqua; -fx-stroke: black; -fx-stroke-width: 0.5;");
        rect.setOnMouseClicked(clickedHandler);
    }

    /**
     * Nastaví veľkosť políčka s regálom na zadanú veľkosť v pixeloch.
     * @param width šírka v px
     * @param height výška v px
     */
    public void rectSize(int width, int height){
        rect.setWidth(width);
        rect.setHeight(height);
    }

    /**
     * Vykreslí sám seba do gridu v controlleri.
     */
    public void drawShelf(){
        GridPane grid = controller.getStorageGrid();
        grid.add(rect,shelf.getPosX(),shelf.getPosY());
    }

    /**
     * Vrati shelf ulozeny v tomto policku.
     * @return shelf
     */
    public Shelf getShelf(){
        return shelf;
    }

    /**
     * Ak je kliknute na políčko, zavola metodu storageControlleru.
     */
    EventHandler<MouseEvent> clickedHandler = ev -> {
        controller.ClickedAction(this);
        ev.consume();
    };

    public void drawHeat(GridPane grid){
        grid.add(rect,shelf.getPosX(),shelf.getPosY());
    }

    public void setColor(Color color){
        rect.setStyle("-fx-fill: "+  String.format("rgb(%d,%d,%d)",color.getRed(),color.getGreen(),color.getBlue())+"; -fx-stroke: black; -fx-stroke-width: 0.5;");

    }
}
