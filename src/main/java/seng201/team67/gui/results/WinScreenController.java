package seng201.team67.gui.results;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.setup.StartController;
import seng201.team67.gui.util.ScreenNavigator;

import java.io.IOException;

/**
 * Controls the win screen view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class WinScreenController {

    /** FXML reference for the label name control. */
    @FXML private Label labelName;
    /** FXML reference for the tour count control. */
    @FXML private Label tourCount;
    /** FXML reference for the concert count control. */
    @FXML private Label concertCount;
    /** FXML reference for the final score control. */
    @FXML private Label finalScore;

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    /**
     * Creates a new win screen controller.
     * @param gameEnvironment the active game environment
     */
    public WinScreenController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize()
    {
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        tourCount.setText("Total Tours: " + gameEnvironment.getTourCount());
        concertCount.setText("Total Concerts: " + gameEnvironment.getConcertCount());
        finalScore.setText("Final Score: " + gameEnvironment.getGameScore());
    }

    /**
     * Quits the game
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void quitGame(ActionEvent event) throws IOException
    {
        System.exit(0);
    }

    /**
     * Starts a new game.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void startNewGame(ActionEvent event) throws IOException
    {
        GameEnvironment gameEnvironment = new GameEnvironment();
        screenNavigator.navigate(event, "/fxml/setup/startmenu.fxml", new StartController());
    }
}
