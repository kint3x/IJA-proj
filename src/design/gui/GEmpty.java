package design.gui;

import design.controllers.StorageController;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class GEmpty {
    private Rectangle rect;
    private StorageController controller;


    public int xPos;
    public int yPos;

    public GEmpty(StorageController c){

        controller=c;
        rect = new Rectangle();
        rectDefaultstyle();
    }

    /**
     * Nastavenie defaultnych nastaveni prazdneho políčka
     */
    private void rectDefaultstyle(){
        rect.setHeight(controller.getRect_s());
        rect.setWidth(controller.getRect_s());
        rect.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.5;");
    }

    /**
     * Nastavi veľkost políčka na zadanú veľkost.
     * @param width šírka
     * @param height výška
     */
    public void rectSize(int width, int height){
        rect.setWidth(width);
        rect.setHeight(height);
    }

    /**
     * Vykresli prazdne policko na gridpane
     * @param x poz x
     * @param y poz y
     */
    public void drawEmpty(int x, int y){
        xPos=x;
        yPos=y;
        GridPane grid = controller.getStorageGrid();
        grid.add(rect,x,y);
    }
}