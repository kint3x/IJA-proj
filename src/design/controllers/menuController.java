package design.controllers;

import design.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class menuController {
    @FXML
    private MenuBar menuBar;
    /**
     * Pri stlačení tlačidla O aplikácií otvorí nové okno s designom about.fxml
     *
     */
    @FXML void handleAboutAction(){
        provideAboutFunctionality();
    }

    @FXML
    public void provideAboutFunctionality()
    {
        try {
            Parent a = FXMLLoader.load(getClass().getResource("/design/layouts/about.fxml"));
            Stage about = new Stage();
            about.setTitle("O aplikácií");
            about.initStyle(StageStyle.UTILITY);
            about.setScene(new Scene(a));
            about.show();

        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

    @FXML
    protected void initialize(){
        Stage primaryStage = Main.getStage();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Controller.menu=this; //Do hlavneho kontrolera vloží svoju inštanciu
    }

}
