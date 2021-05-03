package design.gui;

import design.controllers.Controller;
import design.controllers.StorageController;
import design.model.Cart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GCart implements PropertyChangeListener {

    private StorageController controller;
    private Cart cart;

    private int lNewX;
    private int lNewY;

    private int x;
    private int x;


    public GCart(Cart c, Controller con){
        controller = con;
        cart=c;

        cart.
    }


    private drawCart(){

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
