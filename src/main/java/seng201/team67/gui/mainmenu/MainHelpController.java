package seng201.team67.gui.mainmenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainHelpController {

    Pane helpHolder;

    public MainHelpController (Pane helpHolder)
    {
        this.helpHolder = helpHolder;
    }

    @FXML
    public void goBack(ActionEvent event)
    {
        helpHolder.getChildren().clear();
        helpHolder.setDisable(true);
        helpHolder.setVisible(false);
        helpHolder.setManaged(false);
    }
}
