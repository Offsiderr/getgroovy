package seng201.team67.gui.dev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.minigames.SoundEngineerStandoffController;
import seng201.team67.gui.instantiable.minigames.MicTimingController;
import seng201.team67.gui.instantiable.minigames.CrowdHypeController;
import seng201.team67.gui.instantiable.minigames.CrowdWaveController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.enums.Minigame;
import seng201.team67.services.gameplay.MinigamesService;

public class MiniGameScreenController {

    private final Minigame minigame;
    private final GameEnvironment gameEnvironment;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    @FXML private AnchorPane miniGameAnchorPane;

    public MiniGameScreenController(Minigame minigame, GameEnvironment gameEnvironment)
    {
        this.minigame = minigame;
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    private void initialize()
    {
        loadMiniGame();
    }

    @FXML
    public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGames.fxml", new MiniGamesController(gameEnvironment));
    }

    private void loadMiniGame()
    {
        Parent miniGameView = switch (minigame)
        {
            case SOUNDENGINEER -> viewLoader.load(
                    minigame.path(),
                    new SoundEngineerStandoffController(
                            new MinigamesService(minigame),
                            result -> loadMiniGame(),
                            gameEnvironment
                    )
            );
            case MICTIMING -> viewLoader.load(
                    minigame.path(),
                    new MicTimingController(result -> loadMiniGame())
            );
            case CROWDHYPE -> viewLoader.load(
                    "/fxml/minigames/CrowdHype.fxml",
                    new CrowdHypeController(
                            result -> loadMiniGame()
                    )
            );
            case CROWDWAVE -> viewLoader.load(
                    "/fxml/minigames/CrowdWave.fxml",
                    new CrowdWaveController(
                            result -> loadMiniGame()
                    )
            );
        };

        miniGameAnchorPane.getChildren().setAll(miniGameView);
    }
}