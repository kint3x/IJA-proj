package design.controllers;

import design.model.Storage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import javax.annotation.processing.Generated;

public class storageController {
    private int scale=1;

    @FXML
    private GridPane storageGrid;

    @FXML
    protected void initialize(){
        Storage storage = new Storage();

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
        storageGrid.setVgap(5);
        storageGrid.setHgap(5);
        storageGrid.gridLinesVisibleProperty().set(true);

        //LABEL
        Label nameLabel = new Label("Test");

       storageGrid.add(t,0,0);


    }
}
