package seng201.team67.gui.dev;

import javafx.fxml.FXML;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ScreenNavigator;

import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * Controls the dev functions view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class DevFunctionsController {

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    /**
     * Creates a new dev functions controller.
     * @param gameEnvironment the active game environment
     */
    public DevFunctionsController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Brings up the artists screen from the view artists button.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void showArtists(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/devui/DevArtists.fxml", new DevArtistsController(gameEnvironment));
    }

    /**
     * Brings up the items screen from the view items button.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void showItems(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/devui/DevItems.fxml", new DevItemsController(gameEnvironment));
    }

    /**
     * Brings up the minigames screen from the minigames button
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void miniGames(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGames.fxml", new MiniGamesController(gameEnvironment));
    }

    /**
     * Sends the user back to the main menu
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void goBack(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
