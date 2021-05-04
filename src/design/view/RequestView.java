package design.view;

import design.StorageApp;
import design.controllers.Controller;
import design.controllers.StorageController;
import design.gui.GShelf;
import design.model.Request;
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

import java.util.ArrayList;
import java.util.HashMap;

public class RequestView {

    private Stage requestView;




    public RequestView(){
        requestView = new Stage();

    }

    /**
     * Pripraví stage na zobrazenie pridavanie požiadaviek
     */
    public void prepareStage_info(){
        this.requestView.setTitle("Nová požiadavka");
        this.requestView.setWidth(400);
        this.requestView.setHeight(300);
    }
    /**
     * Funkcia vykreslí pridávanie požiadaviek
     * @param info odkaz na objekt sceny
     * @param root odkaz na otcovsky objekt sceny
     */
    public void drawScene_info(Scene info, Group root){

        ScrollPane scrollPane = new ScrollPane();
        root.getChildren().add(scrollPane);


        VBox vbox = new VBox();
        vbox.setSpacing(5);
        scrollPane.setContent(vbox);

        Font f_nadpis = new Font("Verdana",14);


        Label nadpis = new Label();
        nadpis.setFont(f_nadpis);


        nadpis.setText("Pridať požiadavku");
        nadpis.setPadding(new Insets(0,0,10,0));
        vbox.getChildren().add(nadpis);


        //Generovanie pridavania
        TextField fmeno = new TextField();
        fmeno.setPrefHeight(20);
        TextField fpocet = new TextField();
        fpocet.setPrefHeight(20);
        Button fadd = new Button();
        fadd.setPrefHeight(20);
        fadd.setText("Pridať položku");

        Button fprocess = new Button();
        fprocess.setPrefHeight(20);
        fprocess.setText("Spracovať požiadavky");

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
            if(pocet == 0) return;
            Controller.storage.getStorage().addRequest(fmeno.getText(),pocet);

            drawScene_info(info,root);
        });
        fprocess.setOnMouseClicked(event -> {
            Controller.storage.getStorage().getPath().processRequests();
            drawScene_info(info,root);
        });

        for(Request r : Controller.storage.getStorage().getPath().getOpenRequests()){
            HBox hbox = new HBox();
            hbox.setSpacing(20);
            Label nazov = new Label();
            nazov.setText(r.getItemType().getName());
            nazov.setFont(f_nadpis);
            Label pocet = new Label();
            pocet.setFont(f_nadpis);
            pocet.setText(String.format("%d", r.getCount()));
            hbox.getChildren().add(nazov);
            hbox.getChildren().add(pocet);
            vbox.getChildren().add(hbox);
        }


        fadd.setStyle("-fx-cursor: hand;");
        vbox.getChildren().add(lmeno);
        vbox.getChildren().add(fmeno);
        vbox.getChildren().add(lpocet);
        vbox.getChildren().add(fpocet);
        vbox.getChildren().add(fadd);
        vbox.getChildren().add(fprocess);


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
     * Vráti Inštanciu stage pre vykreslenie pridávanie požiadaviek
     * @return Inštancia stage-u
     */
    public Stage getStage(){
        return this.requestView;
    }
}
