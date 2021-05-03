package design.gui;

import design.controllers.StorageController;
import design.model.Cart;
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

        this.x = c.getStartPosX();
        this.y = c.getStartPosY();

        cartIcon = new ImageView("/design/res/cart.png");

        this.setDefStyle();
        int index = cart.getCartPosIndex();
        x = cart.getPathPoints().get(index).getPosX();
        y = cart.getPathPoints().get(index).getPosY();


    }

    private void setDefStyle(){
        cartIcon.setPreserveRatio(true);
    }


    public void drawCart(){
        GridPane grid = controller.getStorageGrid();
        grid.add(cartIcon,x,y);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName()=="posX"){

        }
        else if(evt.getPropertyName()=="posY"){

        }
        else if(evt.getPropertyName()=="load"){

        }
    }
}
