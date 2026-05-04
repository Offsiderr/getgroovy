package seng201.team67.gui.dev;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.MainGameController;
import seng201.team67.gui.MainMenuController;
import seng201.team67.gui.TheStudioController;

import javafx.event.ActionEvent;
import java.io.IOException;

public class DevFunctionsController {

    private GameEnvironment gameEnvironment;

    private Stage stage;
    private Scene scene;

    public DevFunctionsController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML public void showArtists(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/devui/DevArtists.fxml"));
        loader.setController(new DevArtistsController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void goBack(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        loader.setController(new MainMenuController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
