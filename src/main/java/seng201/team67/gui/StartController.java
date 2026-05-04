package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.services.SoundEffectsService;

import java.io.IOException;

public class StartController {

    private final SoundEffectsService soundEffectsService;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    private final GameEnvironment gameEnvironment;

    public StartController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        this.soundEffectsService = new SoundEffectsService(gameEnvironment);
    }

    //When the title screen is clicked... move into setup screen
    @FXML
    public void onStartGame(ActionEvent event) throws IOException {
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/PlayerSelections.fxml", new SetupController(gameEnvironment));
    }

}
