package seng201.team67.gui.dev;

import javafx.fxml.FXML;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ScreenNavigator;

import javafx.event.ActionEvent;
import java.io.IOException;

public class DevFunctionsController {

    private GameEnvironment gameEnvironment;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    public DevFunctionsController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML public void showArtists(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/devui/DevArtists.fxml", new DevArtistsController(gameEnvironment));
    }

    @FXML public void miniGames(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGames.fxml", new MiniGamesController(gameEnvironment));
    }

    @FXML public void goBack(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
