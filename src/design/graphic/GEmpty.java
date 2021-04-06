package design.graphic;

import design.controllers.storageController;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class GEmpty {
    private Rectangle rect;
    private storageController controller;


    public int xPos;
    public int yPos;

    public GEmpty(storageController c){

        controller=c;
        rect = new Rectangle();
        rectDefaultstyle();
    }

    private void rectDefaultstyle(){
        rect.setHeight(controller.getRect_s());
        rect.setWidth(controller.getRect_s());
        rect.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.5;");
    }
    public void rectSize(int width, int height){
        rect.setWidth(width);
        rect.setHeight(height);
    }

    public void drawEmpty(int x, int y){
        xPos=x;
        yPos=y;
        GridPane grid = controller.getStorageGrid();
        grid.add(rect,x,y);
    }
}
