/**
 * Súbor obsahuje triedu pre ovládanie mriežky.
 * @author Martin Matějka
 */

package design.controllers;

import design.StorageApp;
import design.gui.GCart;
import design.gui.GEmpty;
import design.gui.GPathPoint;
import design.gui.GShelf;
import design.model.*;
import design.model.cart.Cart;
import design.model.item.Item;
import design.model.item.ItemType;
import design.model.path.PathPoint;
import design.model.shelf.Shelf;
import design.view.CartView;
import design.view.HeatmapView;
import design.view.RequestView;
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
    private ArrayList<GPathPoint> gPathPoints;
    private ArrayList<GCart> gCarts;

    private Storage storage = new Storage(); // nacitany sklad

    private int rect_s;             // premenna pre velkost policok

    private double initial_coordX;   //premenna pre drag na uchovavanie predoslej pozicie
    private double initial_coordY;  //premenna pre drag na uchovavanie predoslej pozicie

    private StorageView storageView;
    private CartView cartView;
    private RequestView requestView;

    public static float speed = 1;

    @FXML
    private GridPane storageGrid;

    /**
     * Funkcia volaná pri inicializácii controlleru, nastaví listeners.
     */
    @FXML
    protected void initialize(){
        Controller.storage = this;
        storageView = new StorageView();
        cartView = new CartView();
        requestView = new RequestView();

        rect_s = 30; //default rect s = 30

        gShelfs = new ArrayList<GShelf>();
        gEmpty = new ArrayList<GEmpty>();
        gPathPoints = new ArrayList<GPathPoint>();
        gCarts = new ArrayList<GCart>();


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
        for(int i=0;i<gPathPoints.size();i++){
            gPathPoints.get(i).rectSize((int)rect_s,(int)rect_s);
        }
        for(int i=0;i<gCarts.size();i++){
            gCarts.get(i).cartSize((int)rect_s);
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
            System.err.println(e);
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


        for(PathPoint p : storage.getPath().getPoints()){
            GPathPoint newp = new GPathPoint(p.getPosX(),p.getPosY(),this,p);
            newp.drawPoint();
            gPathPoints.add(newp);
        }

        for(Cart c : storage.getPath().getCarts()){
            GCart newc = new GCart(c,this);
            newc.drawCart();
            gCarts.add(newc);
        }

        storageGrid.setMaxSize(1,1); // defaultne nech je grid maly, pridanymi prvkami sa zvacsi
    }

    public void showHeatMap(){
        HeatmapView mapa = new HeatmapView();

        Group root = new Group();
        Scene info = new Scene(root,700,700);

        mapa.drawHeatmap(root,info,this.gShelfs,storage.getWidth(),storage.getHeight());
        mapa.getStage().setScene(info);
        mapa.getStage().setMinWidth(700);
        mapa.getStage().setMinHeight(700);
        mapa.getStage().show();

    }

    /**
     * Vráti instanciu gridu.
     * @return grid
     */
    public GridPane getStorageGrid(){
        return storageGrid;
    }

    /**
     * Vráti inštanciu skladu.
     */
    public Storage getStorage(){
        return  this.storage;
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
        s.addItem(new Item(new ItemType(key)));
    }
    /**
     * Funkcia zníži počet produktu v poličke o 1
     * @param key String produktu
     * @param s polička
     */
    public void decreaseShelfItem(String key,Shelf s){
        s.removeItem(new ItemType(key));
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
    public void blockPoint(int x, int y){
        this.storage.getPath().blockPoint(x,y);
    }

    /**
     * Vráti aktuálnu velkosť políčka.
     * @return velkosť políčka
     */
    public int getRect_s(){
        return rect_s;
    }

    public void clickedGCart(GCart gCart) {
        try {
            cartView.prepareStage_info(gCart);
            Group root = new Group();
            Scene info = new Scene(root,400,300);
            cartView.drawScene_cart(gCart,root);
            cartView.getStage().setScene(info);
            cartView.getStage().show();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hoverGCart(GCart gCart){
        ArrayList<PathPoint> PathPoints = new ArrayList<PathPoint>();
        PathPoints = gCart.getCart().getPathPoints();
        for (PathPoint p : PathPoints){
            int x = p.getPosX();
            int y = p.getPosY();
            for(GPathPoint j : this.gPathPoints){
                if(j.x == x && j.y == y){
                    j.setColorClicked(gCart.getColor());
                }
            }
        }
    }
    public void endHoverGCart(GCart gCart){
        ArrayList<PathPoint> PathPoints = new ArrayList<PathPoint>();
        PathPoints = gCart.getCart().getPathPoints();
        for (PathPoint p : PathPoints){
            int x = p.getPosX();
            int y = p.getPosY();
            for(GPathPoint j : this.gPathPoints){
                if(j.x == x && j.y == y){
                    j.rectDefaultstyle();
                }
            }
        }
    }
    public void importMenuRequests(String path){
        try {
            storage.importRequests(path);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    static public void setCartSpeed(float sp){
        StorageController.speed = sp;
    }

    static public float getSpeed() {
        return StorageController.speed;
    }

    public void requestMenuClick(){
        try {

            requestView.prepareStage_info();
            Group root = new Group();
            Scene info = new Scene(root,400,300);
            requestView.drawScene_info(info,root);
            requestView.getStage().setScene(info);
            requestView.getStage().show();

        }
        catch (Exception e) {
            System.out.println("EXCEPT");
            System.out.println(e);
        }
    }
}
