package design.gui;

import design.controllers.StorageController;
import design.model.PathPoint;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class GPathPoint {
    private StorageController controller;
    private Rectangle rect;

    private int x;
    private int y;

    private PathPoint pathPoint;

    public GPathPoint(int x, int y,StorageController c){
        this.x = x;
        this.y =y;

        controller=c;
        rect = new Rectangle();
        rectDefaultstyle();
    }

    private void rectDefaultstyle(){
        rect.setHeight(controller.getRect_s());
        rect.setWidth(controller.getRect_s());
        rect.setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 0.5;");
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
     * Vykresli Pathpoint policko na gridpane.
     */
    public void drawPoint(){
        GridPane grid = controller.getStorageGrid();
        grid.add(rect,x,y);
    }

}
