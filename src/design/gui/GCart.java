package design.gui;

import design.controllers.StorageController;
import design.model.Cart;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GCart implements PropertyChangeListener {

    private StorageController controller;
    private Cart cart;

    private ImageView cartIcon;

    private int lNewX;
    private int lNewY;

    private int x;
    private int y;



    public GCart(Cart c, StorageController con){
        controller = con;
        cart=c;

        c.addPropertyChangeListener(this);

        this.x = lNewX = c.getStartPosX();
        this.y = lNewY = c.getStartPosY();

        cartIcon = new ImageView("/design/res/cart.png");

        this.setDefStyle();
    }

    private void setDefStyle(){
        cartIcon.setPreserveRatio(true);
    }


    public void drawCart(){
        Platform.runLater(
                () -> {
                    GridPane grid = controller.getStorageGrid();
                    cartIcon.setFitWidth(controller.getRect_s());
                    grid.getChildren().remove(cartIcon);
                    grid.add(cartIcon,lNewX,lNewY);
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
