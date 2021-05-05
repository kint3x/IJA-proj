/**
 * Súbor obsahuje definíciu triedy StorageApp s metodou main.
 * @author Simon Košina
 */

package design;

import design.controllers.Controller;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Trieda reprezentujúca aplikáciu.
 */
public class StorageApp extends Application {
    public static Stage act_stage;
    public static Scene mainScene;

    public static boolean DragLock;

    public static String file_path;

    /**
     * Volaná po štarte aplikácie.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        init_vars();
        act_stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/sample.fxml"));
        Parent root = loader.load();

        Controller mainController = loader.getController();

        primaryStage.setTitle("Správca skladu 2.0");

        mainScene = new Scene(root, 1280, 720);

        DragLock=true;
       // Zachytáva stlačenia a volá funkcie ktoré obslúžia tieto klávesy
       mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case CONTROL:    DragLock=false; break;
                    case F1: mainController.menu.provideAboutFunctionality(); break;
                }
            }
        });
        mainScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case CONTROL: DragLock=true; break;
                }
            }
        });

        primaryStage.setScene(mainScene);
        primaryStage.show();

        file_path=null;
    }

    /**
     * Spustí aplikáciu.
     * @param args parametre príkazovej riadky
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Vráti aktuálny stage.
     * @return stage
     */
    public static Stage getStage(){
        return act_stage;
    }

    /**
     * Inicializuje všetky premenné
     */
    private void init_vars(){
        this.file_path = "";
    }
}
