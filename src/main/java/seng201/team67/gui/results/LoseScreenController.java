package seng201.team67.gui.results;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.setup.StartController;
import seng201.team67.gui.util.ScreenNavigator;

import java.io.IOException;

public class LoseScreenController {

    @FXML private Label tourCount;
    @FXML private Label concertCount;

    private final GameEnvironment gameEnvironment;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();


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
        screenNavigator.navigate(event, "/fxml/setup/startmenu.fxml", new StartController());
    }
}
