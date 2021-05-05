package design.view;

import design.gui.GCart;
import design.model.cart.CartLoad;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CartView {

    private Stage cart_stage;
    private ArrayList<CartLoad> cartLoad;

    private GCart last_cart;
    private Group last_group;

    /**
     * Konštuktor pre triedu, ktorá zobrazuje obsah košíka
     */
    public CartView(){
        this.cart_stage=new Stage();
        last_cart=null;
        last_group=null;
    }

    /**
     * Pripraví stage na zobrazenie itemov v košíku
     * @param c inštancia košíku
     */
    public void prepareStage_info(GCart c){
        this.cart_stage.setTitle("Košík");
        this.cart_stage.setWidth(400);
        this.cart_stage.setHeight(300);
    }

    /**
     * Vykreslí scénu zadaného košíka
     * @param gc inštancia grafického košíka
     * @param root skupina, do ktorej je scéna vykreslovaná
     */
    public void drawScene_cart(GCart gc, Group root) {

        this.last_cart = gc;
        this.last_group = root;

        for(int i=0; i<root.getChildren().size();i++){
            root.getChildren().remove(i);
        }
        ScrollPane scrollPane = new ScrollPane();
        root.getChildren().add(scrollPane);


        VBox vbox = new VBox();
        vbox.setSpacing(5);
        scrollPane.setContent(vbox);

        Font f_nadpis = new Font("Verdana", 14);


        Label nadpis = new Label();
        nadpis.setFont(f_nadpis);


        nadpis.setText("Názov položky, počet   "+gc.getCart().getLoadedItems()+"/"+gc.getCart().getMaxItems());
        nadpis.setPadding(new Insets(0, 0, 10, 0));
        vbox.getChildren().add(nadpis);

        cartLoad = gc.getCart().getCartLoad();
        if(cartLoad == null) {
            return;
        }
        for (int i=0;i<cartLoad.size();i++) {
            HBox hbox = new HBox();
            hbox.setSpacing(20);
            Label nazov = new Label();
            nazov.setText(cartLoad.get(i).getItem().getType().getName());
            nazov.setFont(f_nadpis);
            Label pocet = new Label();
            pocet.setFont(f_nadpis);
            pocet.setText(String.format("%d", cartLoad.get(i).getCount()));
            hbox.getChildren().add(nazov);
            hbox.getChildren().add(pocet);
            vbox.getChildren().add(hbox);
        }
        scrollPane.setPrefWidth(cart_stage.getWidth());
        cart_stage.widthProperty().addListener((obs,oldVal,newVal) ->{
            scrollPane.setPrefWidth(cart_stage.getWidth());
        });
    }

    /**
     * Vráti Stage, ktorý inštancia používa
     * @return Stage
     */
    public Stage getStage(){
        return this.cart_stage;
    }

    /**
     * Prekreslí obsah košíka znova
     * @param g košík
     */
    public void refreshCart(GCart g){
        if(g.equals(this.last_cart)){
            this.drawScene_cart(last_cart,last_group);
        }
    }
}
