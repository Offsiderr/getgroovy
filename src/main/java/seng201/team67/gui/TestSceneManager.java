package seng201.team67.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestSceneManager {

    private final Stage stage;

    public TestSceneManager(Stage stage) {
        this.stage = stage;
    }

    public void switchToLabelNaming() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/luetestgui/testlabelenter.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 1920, 1080));
        } catch (IOException e) {
            throw new RuntimeException("Could not load scene", e);
        }
    }
}
