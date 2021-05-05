package design.view;

import design.controllers.StorageController;
import design.gui.GShelf;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;

public class StorageView  {

    private Stage info_shelf; // obsahuje view okno na vykreslenie obsahu poličky

    public StorageView(){
        this.info_shelf = new Stage();

    }

    /**
     * Pripraví stage na zobrazenie itemov v poličke
     * @param s grafická inštancia poličky
     */
    public void prepareStage_info(GShelf s){
        this.info_shelf.setTitle("Regál "+ s.getShelf().getPosX()+" "+ s.getShelf().getPosY());
        this.info_shelf.setWidth(400);
        this.info_shelf.setHeight(300);
    }
    /**
     * Funkcia vykreslí Informácie o poličke
     * @param s polička
     * @param info odkaz na objekt sceny
     * @param root odkaz na otcovsky objekt sceny
     */
    public void drawScene_info(GShelf s, Scene info, Group root, StorageController storageController){

        ScrollPane scrollPane = new ScrollPane();
        root.getChildren().add(scrollPane);


        HashMap<String, Integer> hashMap=s.getShelf().getItemCounts();

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        scrollPane.setContent(vbox);

        Font f_nadpis = new Font("Verdana",14);


        Label nadpis = new Label();
        nadpis.setFont(f_nadpis);


        nadpis.setText("Názov položky, počet, +1 , -1 , vymazať");
        nadpis.setPadding(new Insets(0,0,10,0));
        vbox.getChildren().add(nadpis);

        for (String key : hashMap.keySet()){
            HBox hbox = new HBox();
            hbox.setSpacing(20);
            Label nazov = new Label();
            nazov.setText(key);
            nazov.setFont(f_nadpis);
            Label pocet = new Label();
            pocet.setFont(f_nadpis);
            pocet.setText(String.format("%d",s.getShelf().countItems(key)));

            ImageView cross = new ImageView("/design/res/close.png");
            cross.setPreserveRatio(true);
            cross.setFitWidth(14);
            cross.setOnMouseClicked(event -> {
                storageController.deleteShelfItem(key,s.getShelf());
                drawScene_info(s,info,root,storageController);
            });
            cross.setStyle("-fx-cursor: hand;");

            ImageView minus = new ImageView("/design/res/minus.png");
            minus.setPreserveRatio(true);
            minus.setFitWidth(14);
            minus.setOnMouseClicked(event -> {
                storageController.decreaseShelfItem(key,s.getShelf());
                drawScene_info(s,info,root,storageController);
            });
            minus.setStyle("-fx-cursor: hand;");

            ImageView plus = new ImageView("/design/res/plus.png");
            plus.setPreserveRatio(true);
            plus.setFitWidth(14);
            plus.setOnMouseClicked(event -> {
                storageController.increaseShelfItem(key,s.getShelf());
                drawScene_info(s,info,root,storageController);
            });
            plus.setStyle("-fx-cursor: hand;");


            hbox.getChildren().add(nazov);
            hbox.getChildren().add(pocet);
            hbox.getChildren().add(plus);
            hbox.getChildren().add(minus);
            hbox.getChildren().add(cross);

            vbox.getChildren().add(hbox);
            Separator sep = new Separator();
            sep.setOrientation(Orientation.HORIZONTAL);
            vbox.getChildren().add(sep);
        }
        //Generovanie pridavania
        TextField fmeno = new TextField();
        fmeno.setPrefHeight(20);
        TextField fpocet = new TextField();
        fpocet.setPrefHeight(20);
        Button fadd = new Button();
        fadd.setPrefHeight(20);
        fadd.setText("Pridať položku");

        Label lmeno = new Label();
        lmeno.setText("Názov položky:");
        Label lpocet = new Label();
        lpocet.setText("Počet:");

        fmeno.setPrefWidth(info.getWidth()-10);
        fpocet.setPrefWidth(50);
        fadd.setPrefWidth(info.getWidth()-10);

        fadd.setOnMouseClicked(event -> {
            int pocet = 0;
            try{
                pocet = Integer.valueOf(fpocet.getText());
            }
            catch (Exception e){
                pocet =0;
            }
            storageController.addItemToShelf(fmeno.getText(),pocet,s.getShelf());
            drawScene_info(s,info,root,storageController);
        });
        fadd.setStyle("-fx-cursor: hand;");
        vbox.getChildren().add(lmeno);
        vbox.getChildren().add(fmeno);
        vbox.getChildren().add(lpocet);
        vbox.getChildren().add(fpocet);
        vbox.getChildren().add(fadd);


        // nastavenie responzivity
        scrollPane.setPrefWidth(info.getWidth());
        scrollPane.setPrefHeight(info.getHeight());

        info.widthProperty().addListener((obs,oldVal,newVal) ->{
            scrollPane.setPrefWidth(info.getWidth());
            fmeno.setPrefWidth(info.getWidth()-10);
            fpocet.setPrefWidth(50);
            fadd.setPrefWidth(info.getWidth()-10);
        });
        info.heightProperty().addListener((obs,oldVal,newVal) ->{
            scrollPane.setPrefHeight(info.getHeight());
        });
        //koniec nastavenia

    }

    /**
     * Vráti Inštanciu stage pre vykreslenie vecí v poličke
     * @return Inštancia stage-u
     */
    public Stage getInfoShelfStage(){
        return this.info_shelf;
    }

}
