/**
 * Súbor obsahuje triedu pre ovládanie mriežky.
 * @author Martin Matějka
 */

package design.controllers;

import design.StorageApp;
import design.gui.GEmpty;
import design.gui.GShelf;
import design.model.Shelf;
import design.model.Storage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trieda pre ovladač mriežky.
 */
public class StorageController {
    private ArrayList<GShelf> gShelfs; //všetky graficke shelfs
    private ArrayList<GEmpty> gEmpty; // vsetky volne policka na mape
    private Storage storage;          // nacitany sklad

    private int rect_s;             // premenna pre velkost policok

    private double initial_coordX;   //premenna pre drag na uchovavanie predoslej pozicie
    private double initial_coordY;  //premenna pre drag na uchovavanie predoslej pozicie

    private Stage info_shelf;

    @FXML
    private GridPane storageGrid;

    /**
     * Funkcia volaná pri inicializácii controlleru, nastaví listeners.
     */
    @FXML
    protected void initialize(){
        Controller.storage = this;
        this.info_shelf = new Stage();

        rect_s = 30; //default rect s = 30

        gShelfs = new ArrayList<GShelf>();
        gEmpty = new ArrayList<GEmpty>();

        //loadStorage();

        // Obslúži ZOOM a DRAG
        storageGrid.setOnMouseDragged(ev -> {
            if(!StorageApp.DragLock) dragGrid(ev.getX(),ev.getY());
        });

        storageGrid.setOnScroll(scrollEvent -> {
            if(scrollEvent.getDeltaY()>0){
                rect_s = rect_s + 1;
            }
            else rect_s = rect_s - 1 ;

            zoomEvent();
        });


    }

    /**
     * Obslúži zoomovanie, prekreslí kocky na inú veľkosť.
     */
    private void zoomEvent(){
        for(int i=0;i<gShelfs.size();i++){
            gShelfs.get(i).rectSize((int)rect_s,(int)rect_s);
        }
        for(int i=0;i<gEmpty.size();i++){
            gEmpty.get(i).rectSize((int)rect_s,(int)rect_s);
        }
    }

    /**
     * Obslúži drag a zmení polohu Gridu.
     * @param X Aktuálna pozícia myši X
     * @param Y Aktuálna pozícia myši Y
     */
    private void dragGrid(double X, double Y){
        double changex = X - initial_coordX;
        double changey = Y - initial_coordY;

        storageGrid.setTranslateX(storageGrid.getTranslateX() + changex - storage.getWidth()*rect_s/2);
        storageGrid.setTranslateY(storageGrid.getTranslateY() + changey - storage.getHeight()*rect_s/2);

    }

    /**
     * Načíta Storage (neskôr z nastavení).
     */
    public void loadStorage(String path){
        storage = new Storage();

        try {
            storage.importShelfs(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            storage.importItems(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        storage.printStorage();

        storageGrid.setPadding(new Insets(10,10,10,10));


        drawMap();

    }

    /**
     * Vykreslí načítanú mapu s políčkami.
     */
    public void drawMap(){
        ArrayList<Shelf> shelfs= storage.getAllShelfs();

        for(int i=0;i<storage.getWidth();i++){
            for(int j=0;j<storage.getHeight();j++){
                GEmpty addempty= new GEmpty(this);
                addempty.drawEmpty(i,j);
                gEmpty.add(addempty);
            }
        }


        for(int i=0; i < shelfs.size(); i++ )
        {
            GShelf news = new GShelf(shelfs.get(i),this);
            news.drawShelf();
            gShelfs.add(news);
        }
        storageGrid.setMaxSize(1,1); // defaultne nech je grid maly, pridanymi prvkami sa zvacsi
    }

    /**
     * Vráti instanciu gridu.
     * @return grid
     */
    public GridPane getStorageGrid(){
        return storageGrid;
    }

    /**
     * Akcia volaná ak sa klikne na shelfu.
     * @param s Gshelfa ktorá akciu zavolala
     */
    public void ClickedAction(GShelf s){
        System.out.println("Kliknutie na shelfu: \n");
        s.getShelf().printShelf();
        // Po kliknutí na shelf sa zobrazí vnútro ako popup
        try {

            info_shelf.setTitle("Regál "+ s.getShelf().getPosX()+" "+ s.getShelf().getPosY());
            info_shelf.setWidth(400);
            info_shelf.setHeight(300);
            Group root = new Group();
            Scene info = new Scene(root,400,300);
            drawScene_info(s,info,root);
            info_shelf.setScene(info);

            info_shelf.show();

        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Funkcia vykreslí Informácie o poličke
     * @param s polička
     * @param info odkaz na objekt sceny
     * @param root odkaz na otcovsky objekt sceny
     */
    private void drawScene_info(GShelf s,Scene info, Group root){

        ScrollPane scrollPane = new ScrollPane();
        root.getChildren().add(scrollPane);

        // nastavenie responzivity
        scrollPane.setPrefWidth(info.getWidth());
        scrollPane.setPrefHeight(info.getHeight());

        info.widthProperty().addListener((obs,oldVal,newVal) ->{
            scrollPane.setPrefWidth(info.getWidth());
        });
        info.heightProperty().addListener((obs,oldVal,newVal) ->{
            scrollPane.setPrefHeight(info.getHeight());
        });
        //koniec nastavenia

        HashMap <String, Integer> hashMap=s.getShelf().getHashMap();

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
                deleteShelfItem(key,s.getShelf());
                drawScene_info(s,info,root);
            });
            cross.setStyle("-fx-cursor: hand;");

            ImageView minus = new ImageView("/design/res/minus.png");
            minus.setPreserveRatio(true);
            minus.setFitWidth(14);
            minus.setOnMouseClicked(event -> {
                decreaseShelfItem(key,s.getShelf());
                drawScene_info(s,info,root);
            });
            minus.setStyle("-fx-cursor: hand;");

            ImageView plus = new ImageView("/design/res/plus.png");
            plus.setPreserveRatio(true);
            plus.setFitWidth(14);
            plus.setOnMouseClicked(event -> {
                increaseShelfItem(key,s.getShelf());
                drawScene_info(s,info,root);
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
    }

    /**
     * Funkcia vymaže z poličky produkt
     * @param key String produktu
     * @param s polička
     */
    private void deleteShelfItem(String key,Shelf s){
       System.out.println(String.format("DELETE %s",key));
    }
    /**
     * Funkcia zvyši počet produktu v poličke o 1
     * @param key String produktu
     * @param s polička
     */
    private void increaseShelfItem(String key,Shelf s){
        System.out.println(String.format("INCREASE %s",key));
        s.updateCounts(key,1);
    }
    /**
     * Funkcia zníži počet produktu v poličke o 1
     * @param key String produktu
     * @param s polička
     */
    private void decreaseShelfItem(String key,Shelf s){
        System.out.println(String.format("DECREASE %s",key));
        s.updateCounts(key,-1);
    }


    /**
     * Vráti aktuálnu velkosť políčka.
     * @return velkosť políčka
     */
    public int getRect_s(){
        return rect_s;
    }

}
