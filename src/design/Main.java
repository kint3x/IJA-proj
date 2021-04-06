package design;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage act_stage;
    private static Scene mainScene;

    public static boolean DragLock;

    @Override
    public void start(Stage primaryStage) throws Exception{
        act_stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("layouts/sample.fxml"));
        primaryStage.setTitle("Správca skladu 2.0");
        mainScene = new Scene(root, 1280, 720);

        DragLock=true;

       mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case CONTROL:    DragLock=false; break;
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
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage(){
        return act_stage;
    }
}
