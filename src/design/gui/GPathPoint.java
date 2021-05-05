package design.gui;

import design.controllers.StorageController;
import design.model.path.PathPoint;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

public class GPathPoint {
    private StorageController controller;
    private Circle circle;

    public int x;
    public int y;

    private PathPoint pathPoint;


    public GPathPoint(int x, int y,StorageController c,PathPoint p){
        this.x = x;
        this.y =y;
        pathPoint=p;

        controller=c;
        circle = new Circle();
        circle.setOnMouseClicked(event -> {
            if(pathPoint.isBlocked()){
                controller.blockPoint(this.x,this.y);
                circle.setStyle("-fx-fill: #8ced97");
            }
            else{
                controller.blockPoint(this.x,this.y);
                circle.setStyle("-fx-fill: #f27983");
            }
        });
        rectDefaultstyle();
    }

    public void rectDefaultstyle(){
        circle.setRadius(controller.getRect_s()/3.0);
        circle.setStyle("-fx-fill: #8ced97");
    }

    public  void setColorClicked(String color){
        circle.setStyle("-fx-fill: "+color+";");
    }




    /**
     * Nastavi veľkost políčka na zadanú veľkost.
     * @param width šírka
     * @param height výška
     */
    public void rectSize(int width, int height){
        circle.setRadius(width/3.0);
    }

    /**
     * Vykresli Pathpoint policko na gridpane.
     */
    public void drawPoint(){
        GridPane grid = controller.getStorageGrid();
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(circle);
        grid.add(hb,x,y);
    }

}
