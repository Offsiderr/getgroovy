package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ScreenNavigator;

import java.io.IOException;

public class WinScreenController {

    @FXML private Label labelName;
    @FXML private Label tourCount;
    @FXML private Label concertCount;

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
    }

    @FXML public void quitGame(ActionEvent event) throws IOException
    {
        System.exit(0);
    }

    @FXML public void startNewGame(ActionEvent event) throws IOException
    {
        GameEnvironment gameEnvironment = new GameEnvironment();
        screenNavigator.navigate(event, "/fxml/startmenu.fxml", new StartController(gameEnvironment));
    }
}
