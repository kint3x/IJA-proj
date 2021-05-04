/**
 * Ovladač menu.
 * @author Martin Matějka
 */

package design.controllers;

import design.StorageApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

/**
 * Trieda, ktorá ovláda menu.
 */
public class MenuController {
    @FXML
    private MenuBar menuBar;
    /**
     * Pri stlačení tlačidla O aplikácií otvorí nové okno s designom about.fxml.
     */
    @FXML void handleAboutAction(){
        provideAboutFunctionality();
    }
    @FXML void handleNacitatAction(){ provideNacitatFunctionality();}

    /**
     * Dodá funkčnosť po kliknutí na "O aplikácií".
     */
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

    @FXML void provideNacitatFunctionality(){
        FileChooser load = new FileChooser();
        load.setTitle("Načítať sklad");
        try {
            File f = load.showOpenDialog(StorageApp.getStage());
            //aplikacia cerpa z tohoto suboru
            if (f == null) return;
            StorageApp.file_path = f.getAbsolutePath();
            Controller.storage.loadStorage(StorageApp.file_path);
        }
        catch (Exception e) {
            System.out.println("Nastalo zatvorenie pri načítaní súborov: "+e.getMessage());
        }
    }

    @FXML
    void handlePoziadavkyAction(){
        if(StorageApp.file_path == null) return;
        FileChooser load = new FileChooser();
        load.setTitle("Načítať požiadavky");
        try {
            File f = load.showOpenDialog(StorageApp.getStage());
            //aplikacia cerpa z tohoto suboru
            if (f == null) return;
            Controller.storage.importMenuRequests(f.getAbsolutePath());
        }
        catch (Exception e) {
            System.out.println("Nastalo zatvorenie pri načítaní súborov: "+e.getMessage());
        }
    }

    @FXML
    void handleRequestAction(){
        if(StorageApp.file_path == null) return;
        Controller.storage.requestMenuClick();
    }
    @FXML
    void handleHeatmapAction(){
        if(StorageApp.file_path == null) return;
        Controller.storage.showHeatMap();
    }

    /**
     * Inicializácia ovladača. Vloží svoju inštanciu do statickej premennej hlavného ovladača.
     */
    @FXML
    protected void initialize(){
        Stage primaryStage = StorageApp.getStage();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Controller.menu=this; //Do hlavneho kontrolera vloží svoju inštanciu
    }

}
