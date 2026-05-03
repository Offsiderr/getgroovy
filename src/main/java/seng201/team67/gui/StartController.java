package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import seng201.team67.GameEnvironment;
import seng201.team67.services.SoundEffectsService;

import java.io.IOException;

public class StartController {

    private SoundEffectsService soundEffectsService = new SoundEffectsService();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final GameEnvironment gameEnvironment;

    public StartController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    //When the title screen is clicked... move into setup screen
    @FXML
    public void onStartGame(ActionEvent event) throws IOException {
        soundEffectsService.playYes();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlayerSelections.fxml"));
        loader.setController(new SetupController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
