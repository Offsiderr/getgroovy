package seng201.team67.gui.dev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.enums.Minigame;

/**
 * Controls the mini games view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MiniGamesController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    /**
     * Creates a new mini games controller.
     * @param gameEnvironment the active game environment
     */
    public MiniGamesController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Plays the Sound Engineer Minigame
     * @param event the action event that triggered the request
     */
    @FXML public void soundEngineer(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGameScreen.fxml",
                new MiniGameScreenController(Minigame.SOUNDENGINEER, gameEnvironment));
    }

    /**
     * Plays the Mic Timing Minigame
     * @param event the action event that triggered the request
     */
    @FXML public void micTiming(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGameScreen.fxml",
                new MiniGameScreenController(Minigame.MICTIMING, gameEnvironment));
    }

    /**
     * Plays the Crowd Hype Minigame
     * @param event the action event that triggered the request
     */
    @FXML public void crowdHype(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGameScreen.fxml",
                new MiniGameScreenController(Minigame.CROWDHYPE, gameEnvironment));
    }

    /**
     * Plays the Crowd Wave Minigame
     * @param event the action event that triggered the request
     */
    @FXML public void crowdWave(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGameScreen.fxml",
                new MiniGameScreenController(Minigame.CROWDWAVE, gameEnvironment));
    }

    /**
     * Sends the player back to the main menu
     * @param event the action event that triggered the request
     */
    @FXML public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}