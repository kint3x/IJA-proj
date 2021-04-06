package design.controllers;

import design.Main;
import design.graphic.GEmpty;
import design.graphic.GShelf;
import design.model.Shelf;
import design.model.Storage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class storageController {
    private ArrayList<GShelf> gShelfs; //všetky graficke shelfs
    private ArrayList<GEmpty> gEmpty; // vsetky volne policka na mape
    private Storage storage;          // nacitany sklad

    private int rect_s;             // premenna pre velkost policok

    private double initial_coordX;   //premenna pre drag na uchovavanie predoslej pozicie
    private double initial_coordY;  //premenna pre drag na uchovavanie predoslej pozicie

    @FXML
    private GridPane storageGrid;

    @FXML
    protected void initialize(){
        rect_s = 30; //default rect s = 30

        gShelfs = new ArrayList<GShelf>();
        gEmpty = new ArrayList<GEmpty>();

        loadStorage();

        /** Obslúži ZOOM a DRAG **/
        storageGrid.setOnMouseDragged(ev -> {
            if(!Main.DragLock) dragGrid(ev.getX(),ev.getY());
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
     * Obslúži zoomovanie, prekreslí kocky na inú veľkosť
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
     * Obslúži drag a zmení polohu Gridu
     * @param Aktuálna pozícia myši X
     * @param Aktuálna pozícia myši Y
     */
    private void dragGrid(double X, double Y){
        double changex = X - initial_coordX;
        double changey = Y - initial_coordY;

        storageGrid.setTranslateX(storageGrid.getTranslateX() + changex - storage.getWidth()*rect_s/2);
        storageGrid.setTranslateY(storageGrid.getTranslateY() + changey - storage.getHeight()*rect_s/2);

    }

    /**
     * Načíta Storage (neskôr z nastavení)
     */
    public void loadStorage(){
        storage = new Storage();

        try {
            storage.importShelfs("input/shelfs.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            storage.importItems("input/items.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        storage.printStorage();

        storageGrid.setPadding(new Insets(10,10,10,10));


       /* Stage primaryStage = Main.getStage();
        storageGrid.prefWidthProperty().bind(primaryStage.widthProperty());*/





        drawMap();

    }

    /**
     * Vykreslí načítanú mapu s políčkami
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

    public GridPane getStorageGrid(){
        return storageGrid;
    }

    /**
     * Akcia volaná ak sa klikne na shelfu
     * @param s Gshelfa ktorá akciu zavolala
     */
    public void ClickedAction(GShelf s){
        System.out.println("Kliknutie na shelfu: \n");
        s.getShelf().printShelf();
    }

    /**
     * Vráti aktuálnu velkosť políčka
     * @return velkosť políčka
     */
    public int getRect_s(){
        return rect_s;
    }

}
