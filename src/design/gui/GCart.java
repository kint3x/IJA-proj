package design.gui;

import design.controllers.StorageController;
import design.model.cart.Cart;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

public class GCart implements PropertyChangeListener {

    private StorageController controller;
    private Cart cart;

    private ImageView cartIcon;
    private Circle farba;

    private String randColor;

    private int lNewX;
    private int lNewY;

    private int x;
    private int y;


    public GCart(Cart c, StorageController con){
        controller = con;
        this.cart=c;

        c.addPropertyChangeListener(this);

        this.x = lNewX = c.getStartPosX();
        this.y = lNewY = c.getStartPosY();

        cartIcon = new ImageView("/design/res/cart.png");
        this.farba = new Circle();

        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        this.randColor = String.format("#%06x", rand_num);

        this.setDefStyle();
    }

    private void setDefStyle(){
        cartIcon.setPreserveRatio(true);
        System.out.println("-fx-background-color: "+this.randColor+";");
        farba.setFill(Paint.valueOf(this.randColor));
        farba.setOnMouseClicked(event -> {
            controller.clickedGCart(this);

        });
        farba.setOnMouseEntered(event -> {
            controller.hoverGCart(this);
        });

        farba.setOnMouseExited(event -> {
            controller.endHoverGCart(this);
        });

    }
    public String getColor(){
        return this.randColor;
    }
    public Cart getCart(){
        return this.cart;
    }

    public void cartSize(int size){
        cartIcon.setFitWidth(size);
        this.farba.setRadius(size/4.0);

    }

    public void drawCart(){
        Platform.runLater(
                () -> {
                    GridPane grid = controller.getStorageGrid();
                    cartIcon.setFitWidth(controller.getRect_s());
                    grid.getChildren().remove(cartIcon);
                    grid.add(cartIcon,lNewX,lNewY);
                    this.farba.setRadius(controller.getRect_s()/4.0);
                    grid.getChildren().remove(farba);
                    grid.add(farba,lNewX,lNewY);

                }
        );
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName()=="posX"){
            this.x=this.lNewX;
            this.lNewX= (int) evt.getNewValue();
            this.drawCart();
        }
        else if(evt.getPropertyName()=="posY"){
            this.y=this.lNewY;
            this.lNewY=(int)evt.getNewValue();
            this.drawCart();

        }
        else if(evt.getPropertyName()=="load"){

        }
    }
}
