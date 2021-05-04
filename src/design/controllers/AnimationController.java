package design.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class AnimationController {

    @FXML
    private Slider slider;

    @FXML
    private Label val;

    @FXML
    protected void initialize(){
        slider.setValue(1.0);
        val.setText("1.0");
        slider.setOnMouseDragged(event -> {
            untilRelease();
            StorageController.setCartSpeed((float)slider.getValue());
        });
    }

    private void untilRelease(){
        val.setText(String.format("%f", (float) slider.getValue()));
    }

    @FXML
    public void handlePlayBtn(){
        Controller.storage.getStorage().getPath().startCarts();
    }

    @FXML
    public void handlePauseBtn(){
        Controller.storage.getStorage().getPath().stopCarts();
    }
}
