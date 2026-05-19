package seng201.team67.gui.results;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.setup.StartController;
import seng201.team67.gui.util.ScreenNavigator;

import java.io.IOException;

public class WinScreenController {

    @FXML private Label labelName;
    @FXML private Label tourCount;
    @FXML private Label concertCount;
    @FXML private Label finalScore;

    private final GameEnvironment gameEnvironment;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    public WinScreenController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize()
    {
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        tourCount.setText("Total Tours: " + gameEnvironment.getTourCount());
        concertCount.setText("Total Concerts: " + gameEnvironment.getConcertCount());
        finalScore.setText("Final Score: " + gameEnvironment.getGameScore());
    }

    @FXML public void quitGame(ActionEvent event) throws IOException
    {
        System.exit(0);
    }

    @FXML public void startNewGame(ActionEvent event) throws IOException
    {
        GameEnvironment gameEnvironment = new GameEnvironment();
        screenNavigator.navigate(event, "/fxml/setup/startmenu.fxml", new StartController());
    }
}
