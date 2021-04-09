package design;

import design.controllers.Controller;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Trieda reprezentujúca aplikáciu.
 */
public class StorageApp extends Application {
    public static Stage act_stage;
    public static Scene mainScene;

    public static boolean DragLock;

    /**
     * Volaná po štarte aplikácie.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
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
        // Ukážka čo sme vypracovali
        try {
            Parent a = FXMLLoader.load(getClass().getResource("/design/layouts/proj2.fxml"));
            Stage about = new Stage();
            about.setTitle("Demonstrační třída");
            about.initStyle(StageStyle.UTILITY);
            about.setScene(new Scene(a));
            about.show();

        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage(){
        return act_stage;
    }

}
