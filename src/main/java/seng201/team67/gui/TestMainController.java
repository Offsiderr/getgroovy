package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class TestMainController {

    private TestSceneManager sceneManager;

    public void init(Stage stage) {
        this.sceneManager = new TestSceneManager(stage);
    }

    @FXML
    private void onStartGame() {
        sceneManager.switchToLabelNaming();
    }
}
