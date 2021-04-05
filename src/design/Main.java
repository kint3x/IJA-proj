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

        launch(args);
    }

    public static Stage getStage(){
        return act_stage;
    }
}
