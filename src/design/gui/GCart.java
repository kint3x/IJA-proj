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

    /**
     * Konštruktor triedy Grafického vozíka obsahuje tvar vozíka, jeho farbu
     * @param c vykreslovaný košík
     * @param con inštancia storage controlleru
     */
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

    /**
     * Nastaví východzí štýl vozíka
     */
    private void setDefStyle(){
        cartIcon.setPreserveRatio(true);

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

    /**
     * Vráti hexa farbu košíka
     * @return farba košíka
     */
    public String getColor(){
        return this.randColor;
    }

    /**
     * Vráti košík pre ktorý bola trieda vytvorená
     * @return košík
     */
    public Cart getCart(){
        return this.cart;
    }

    /**
     * Nastaví veľkosť obrázka košíka na danú veľkosť
     * @param size veľkosť košíka
     */
    public void cartSize(int size){
        cartIcon.setFitWidth(size);
        this.farba.setRadius(size/4.0);

    }

    /**
     * Vykreslí košík do mriežky skladu
     */
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

    /**
     * Funkcia volaná pri zmene pozície košíka
     * @param evt Zmena stavu
     */
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
        else{
            Platform.runLater(
                    () -> {
                        controller.cartView.refreshCart(this);
                    }
            );

        }
    }
}
