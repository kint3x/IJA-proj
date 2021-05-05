package design.controllers;

import design.StorageApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class AnimationController {

    @FXML
    private Slider slider;

    @FXML
    private Label val;

    @FXML
    private Label recordCounter;

    private int allStates;
    private int currState;
    private int currIndex;
    private boolean history;

    @FXML
    protected void initialize(){
        slider.setValue(1.0);
        val.setText("1.0");
        slider.setOnMouseDragged(event -> {
            untilRelease();
            StorageController.setCartSpeed((float) slider.getValue());
        });

        allStates=0;
        currState=0;
        currIndex=0;
        history=false;
    }

    /**
     * Funkcia vykresľuje hodnotu slideru
     */
    private void untilRelease(){
        val.setText(String.format("%.3f", (float) slider.getValue()));
    }

    /**
     * Funkcia obslúži tlačítko Play
     */
    @FXML
    public void handlePlayBtn(){
        Controller.storage.getStorage().getPath().startCarts();
    }
    /**
     * Funkcia obslúži tlačítko Pause
     */
    @FXML
    public void handlePauseBtn(){
        Controller.storage.getStorage().getPath().stopCarts();
    }
    /**
     * Funkcia obslúži tlačítko Record
     */
    @FXML
    public void handleRecordBtn(){
        Controller.storage.getStorage().getCareTaker().saveState();
        addState();
    }
    /**
     * Funkcia obslúži tlačítko Prev
     */
    @FXML
    public void handlePrevBtn(){
        if(currIndex-1 < 0) return;
        currIndex--;
        currState--;
        Controller.storage.getStorage().getCareTaker().setState(currIndex);
        drawState();
    }
    /**
     * Funkcia obslúži tlačítko Next
     */
    @FXML
    public void handleNextBtn(){
        if(!history){
            history=true;
            currState++;
        }
        if(currIndex+1 >= allStates) return;
        currIndex++;
        currState++;
        Controller.storage.getStorage().getCareTaker().setState(currIndex);
        drawState();
    }

    /**
     * Funkcia obnoví počet nahraných stavov
     */
    public void addState(){
        if(StorageApp.file_path == null) return;
        allStates++;
        drawState();
    }
    /**
     * Funkcia vykreslí label so stavmi
     */
    public void drawState(){
        recordCounter.setText(currState + "/" + allStates);
    }

}
