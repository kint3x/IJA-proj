package design.controllers;

import design.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class menuController {
    @FXML
    private MenuBar menuBar;


    /**
     * Handluje event na tlačítku O aplikácií
     *
     * @param event Event na tlačítku O aplikácií v menu.
     */
    @FXML
    private void handleAboutAction(final ActionEvent event)
    {

        provideAboutFunctionality();
    }

    /**
     * Handluje CTRL+A pre O aplikáci
     *
     * @param event Input event.
     */
    @FXML
    private void handleKeyInput(final InputEvent event)
    {
        if (event instanceof KeyEvent)
        {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A)
            {
                provideAboutFunctionality();
            }
        }
    }

    /**
     * Perform functionality associated with "About" menu selection or CTRL-A.
     */
    @FXML
    private void provideAboutFunctionality()
    {

        System.out.println("Kliknutie na about!");
    }

    @FXML
    protected void initialize(){
        Stage primaryStage = Main.getStage();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
    }
}
