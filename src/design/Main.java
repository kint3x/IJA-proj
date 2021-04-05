package design;

import design.model.Storage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
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

        storage.printShelfs();

        launch(args);
    }
}
