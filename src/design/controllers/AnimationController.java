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
        });
        slider.setOnDragDone(event -> {
            Controller.storage.setCartSpeed((float)slider.getValue());
        });

    }

    private void untilRelease(){
        val.setText(String.format("%f", (float) slider.getValue()));
    }

    @FXML
    public void handlePlayBtn(){
        System.out.println("TEST");
    }

    @FXML
    public void handlePauseBtn(){
        System.out.println("TEST2");
    }
}
