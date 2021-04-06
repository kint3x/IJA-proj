package design.graphic;


import design.controllers.storageController;
import design.model.Shelf;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class GShelf {
    private Shelf shelf;
    private Rectangle rect;
    private storageController controller;


    public GShelf(Shelf s, storageController c){

        controller=c;
        shelf = s;
        rect = new Rectangle();
        rectDefaultstyle();
    }

    private void rectDefaultstyle(){
        rect.setHeight(controller.getRect_s());
        rect.setWidth(controller.getRect_s());
        rect.setStyle("-fx-fill: aqua; -fx-stroke: black; -fx-stroke-width: 0.5;");
        rect.setOnMouseClicked(clickedHandler);
    }

    public void rectSize(int width, int height){
        rect.setWidth(width);
        rect.setHeight(height);
    }

    public void drawShelf(){
        GridPane grid = controller.getStorageGrid();
        grid.add(rect,shelf.getPosX(),shelf.getPosY());
    }

    public Shelf getShelf(){
        return shelf;
    }

    EventHandler<MouseEvent> clickedHandler = ev -> {
        controller.ClickedAction(this);
        ev.consume();
    };

}
