package design.view;

import design.gui.GCart;
import design.model.CartLoad;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CartView {

    private Stage cart_stage;
    private CartLoad cartLoad;

    public CartView(){
        this.cart_stage=new Stage();

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

    public void drawScene_cart(GCart gc, Group root) {

        ScrollPane scrollPane = new ScrollPane();
        root.getChildren().add(scrollPane);


        VBox vbox = new VBox();
        vbox.setSpacing(5);
        scrollPane.setContent(vbox);

        Font f_nadpis = new Font("Verdana", 14);


        Label nadpis = new Label();
        nadpis.setFont(f_nadpis);


        nadpis.setText("Názov položky, počet");
        nadpis.setPadding(new Insets(0, 0, 10, 0));
        vbox.getChildren().add(nadpis);
        cartLoad = gc.getCart().getCartLoad();
        if(cartLoad == null) {
            return;
        }
        //for (int i=0;i<cartLoad.getCount();i++) {
            HBox hbox = new HBox();
            hbox.setSpacing(20);
            Label nazov = new Label();
            nazov.setText(cartLoad.getItem().getType().getName());
            nazov.setFont(f_nadpis);
            Label pocet = new Label();
            pocet.setFont(f_nadpis);
            pocet.setText(String.format("%d", cartLoad.getCount()));
            hbox.getChildren().add(nazov);
            hbox.getChildren().add(pocet);
            vbox.getChildren().add(hbox);
       // }

    }
    public Stage getStage(){
        return this.cart_stage;
    }

}
