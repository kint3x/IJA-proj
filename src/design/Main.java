package design;

import design.model.Storage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage act_stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        act_stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("layouts/sample.fxml"));
        primaryStage.setTitle("Správca skladu 2.0");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage(){
        return act_stage;
    }
}
