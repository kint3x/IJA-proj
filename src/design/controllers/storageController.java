package design.controllers;

import design.model.Shelf;
import design.model.Storage;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import javax.annotation.processing.Generated;
import java.util.ArrayList;

public class storageController {
    private int scale=1;
    private Storage storage;

    @FXML
    private GridPane storageGrid;

    @FXML
    protected void initialize(){
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

        Rectangle t = new Rectangle();
        t.setHeight(50);
        t.setWidth(50);
        t.setArcHeight(5);
        t.setArcWidth(5);

        t.setFill(Color.AQUA);

        storageGrid.setPadding(new Insets(10,10,10,10));
        storageGrid.setVgap(30);
        storageGrid.setHgap(30);
        storageGrid.gridLinesVisibleProperty().set(true);


        // storageGrid.add(t,0,0);
        addShelfs();
        handleMouseClick();
    }
    public void addShelfs(){
        ArrayList<Shelf> shelfs= storage.getAllShelfs();
        for(int i=0; i < shelfs.size(); i++ )
        {
            Rectangle t = new Rectangle();
            t.setHeight(30);
            t.setWidth(30);
            t.setArcHeight(5);
            t.setArcWidth(5);
            t.setFill(Color.AQUA);

            storageGrid.add(t,shelfs.get(i).getPosX(),shelfs.get(i).getPosY());
        }
    }

}
