package seng201.team67.gui.mainmenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Controls the main help view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MainHelpController {

    /** FXML reference for the help holder control. */
    Pane helpHolder;

    /**
     * Creates a new main help controller.
     * @param helpHolder the help holder
     */
    public MainHelpController (Pane helpHolder)
    {
        this.helpHolder = helpHolder;
    }

    /**
     * Sends the player back to the main menu
     * @param event the action event that triggered the request
     */
    @FXML
    public void goBack(ActionEvent event)
    {
        helpHolder.getChildren().clear();
        helpHolder.setDisable(true);
        helpHolder.setVisible(false);
        helpHolder.setManaged(false);
    }
}
