package seng201.team67.gui.dev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.enums.Minigame;


public class MiniGamesController {

    private final GameEnvironment gameEnvironment;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    public MiniGamesController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }


    @FXML public void soundEngineer(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGameScreen.fxml",
                new MiniGameScreenController(Minigame.SOUNDENGINEER, gameEnvironment));
    }

    @FXML public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
