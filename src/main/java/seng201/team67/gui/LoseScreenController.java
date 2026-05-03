package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;

import java.io.IOException;

public class LoseScreenController {

    @FXML private Label tourCount;
    @FXML private Label concertCount;

    private final GameEnvironment gameEnvironment;
    private Stage stage;
    private Scene scene;


    public LoseScreenController (GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize() {
        tourCount.setText("Total Tours: " + gameEnvironment.getTourCount());
        concertCount.setText("Total Concerts: " + gameEnvironment.getConcertCount());
    }

    @FXML public void quitGame(ActionEvent event) throws IOException
    {
        System.exit(0);
    }

    @FXML public void startNewGame(ActionEvent event) throws IOException {

        GameEnvironment gameEnvironment = new GameEnvironment();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/startmenu.fxml"));
        loader.setController(new StartController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
