/**
 * Súbor obsahuje triedu pre ovládanie mriežky.
 * @author Martin Matějka
 */

package design.controllers;

import design.StorageApp;
import design.gui.GEmpty;
import design.gui.GShelf;
import design.model.Item;
import design.model.ItemType;
import design.model.Shelf;
import design.model.Storage;
import design.view.StorageView;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

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

    private StorageView storageView;

    @FXML
    private GridPane storageGrid;

    /**
     * Funkcia volaná pri inicializácii controlleru, nastaví listeners.
     */
    @FXML
    protected void initialize(){
        Controller.storage = this;
        storageView = new StorageView();


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
            storage.importFile(path);
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

            storageView.prepareStage_info(s);
            Group root = new Group();
            Scene info = new Scene(root,400,300);
            storageView.drawScene_info(s,info,root,this);
            storageView.getInfoShelfStage().setScene(info);
            storageView.getInfoShelfStage().show();

        }
        catch (Exception e) {
            System.out.println("EXCEPT");
            System.out.println(e);
        }

    }


    /**
     * Funkcia vymaže z poličky produkt
     * @param key String produktu
     * @param s polička
     */
    public void deleteShelfItem(String key,Shelf s){
        ItemType itemType = new ItemType(key);
        s.deleteItems(itemType);
    }
    /**
     * Funkcia zvyši počet produktu v poličke o 1
     * @param key String produktu
     * @param s polička
     */
    public void increaseShelfItem(String key,Shelf s){
        s.updateCounts(key,1);
    }
    /**
     * Funkcia zníži počet produktu v poličke o 1
     * @param key String produktu
     * @param s polička
     */
    public void decreaseShelfItem(String key,Shelf s){
        s.updateCounts(key,-1);
    }

    /**
     * Pridá item do poličky
     * @param key názov itemu
     * @param s inštancia poličky
     */
    public void addItemToShelf(String key,int pocet,Shelf s){
        if(key == "") return;
        if(pocet <= 0) return;

        ItemType itemType = new ItemType(key);
        Item item = new Item(itemType);
        s.addItem(item,pocet);

    }

    /**
     * Vráti aktuálnu velkosť políčka.
     * @return velkosť políčka
     */
    public int getRect_s(){
        return rect_s;
    }

}
