/**
 * Súbor obsahuje triedu s hlavným ovladačom.
 * @author Martin Matějka
 */

package design.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * Uchováva inštancie vnorených ovladačov.
 */
public class Controller {
    @FXML
    private AnchorPane AppPane;
    public static MenuController menu;

    /**
     * Funkcia volaná po inicializácií aplikácie.
     */
    public void initialize(){


    }

}
