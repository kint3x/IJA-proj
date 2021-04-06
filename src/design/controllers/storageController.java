package design.controllers;

import design.Main;
import design.graphic.GEmpty;
import design.graphic.GShelf;
import design.model.Shelf;
import design.model.Storage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class storageController {
    private ArrayList<GShelf> gShelfs;
    private ArrayList<GEmpty> gEmpty;
    private int scale=1;
    private Storage storage;

    private int rect_s;

    private double initial_coordX;
    private double initial_coordY;

    @FXML
    private GridPane storageGrid;

    @FXML
    protected void initialize(){
        rect_s = 30; //default rect s = 30

        gShelfs = new ArrayList<GShelf>();
        gEmpty = new ArrayList<GEmpty>();
        loadStorage();
        //handleMouseClick();


        storageGrid.setOnMouseDragged(ev -> {
            if(!Main.DragLock) dragGrid(ev.getX(),ev.getY());
        });

        storageGrid.setOnScroll(scrollEvent -> {
            if(scrollEvent.getDeltaY()>0){
                rect_s = rect_s + 1;
            }
            else rect_s = rect_s - 1 ;

            zoomEvent();
            System.out.println(rect_s);
        });


    }

    private void zoomEvent(){
        for(int i=0;i<gShelfs.size();i++){
            gShelfs.get(i).rectSize((int)rect_s,(int)rect_s);
        }
        for(int i=0;i<gEmpty.size();i++){
            gEmpty.get(i).rectSize((int)rect_s,(int)rect_s);
        }
    }

    private void dragGrid(double X, double Y){
        double changex = X - initial_coordX;
        double changey = Y - initial_coordY;

        storageGrid.setTranslateX(storageGrid.getTranslateX() + changex - storage.getWidth()*rect_s/2);
        storageGrid.setTranslateY(storageGrid.getTranslateY() + changey - storage.getHeight()*rect_s/2);

    }


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
        //storageGrid.gridLinesVisibleProperty().set(true);

        Stage primaryStage = Main.getStage();
        storageGrid.prefWidthProperty().bind(primaryStage.widthProperty());




        // storageGrid.add(t,0,0);
        drawMap();
        // handleMouseClick();
    }
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
    }

    public GridPane getStorageGrid(){
        return storageGrid;
    }

    public void ClickedAction(GShelf s){
        System.out.println("Kliknutie na shelfu: \n");
        s.getShelf().printShelf();
    }

    public int getRect_s(){
        return rect_s;
    }

}
