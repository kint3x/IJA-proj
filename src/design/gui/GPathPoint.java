package design.gui;

import design.controllers.StorageController;
import design.model.path.PathPoint;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GPathPoint implements PropertyChangeListener {
    private StorageController controller;
    private Circle circle;

    public int x;
    public int y;

    private PathPoint pathPoint;

    /**
     * Konštruktor triedy Grafického zobrazenia bodu cesty
     * @param x X pozícia
     * @param y Y pozícia
     * @param c Controller skladu
     * @param p Bod cesty pre ktorý má byť vytvorený
     */
    public GPathPoint(int x, int y,StorageController c,PathPoint p){
        this.x = x;
        this.y =y;
        pathPoint=p;
        p.addPropertyChangeListener(this);

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

    /**
     * Nastaví východzí štýl bodu cesty
     */
    public void rectDefaultstyle(){
        circle.setRadius(controller.getRect_s()/3.0);
        if(pathPoint.isBlocked()){
            circle.setStyle("-fx-fill: #f27983");
        }
        else{
            circle.setStyle("-fx-fill: #8ced97");
        }

    }

    /**
     * Nastaví bodu cesty zadanú farbu
     * @param color farba
     */
    public  void setColorClicked(String color){
        circle.setStyle("-fx-fill: "+color+";");
    }

    /**
     * Pri blokovaní cesty sa bod vyfarbí na červeno
     * @param evt event
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName()=="block"){
            if ((boolean) evt.getNewValue()) {
                circle.setStyle("-fx-fill: #8ced97");
            } else {
                circle.setStyle("-fx-fill: #8ced97");
            }
        }
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
