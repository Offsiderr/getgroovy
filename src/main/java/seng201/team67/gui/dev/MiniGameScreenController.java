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

/**
 * Controls the mini game screen view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MiniGameScreenController {

    /** The minigame. */
    private final Minigame minigame;
    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** The view loader. */
    private final ViewLoader viewLoader = new ViewLoader();

    /** FXML reference for the minigame anchor pane control. */
    @FXML private AnchorPane miniGameAnchorPane;

    /**
     * Creates a new minigame screen controller.
     * @param minigame the minigame
     * @param gameEnvironment the active game environment
     */
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

    /**
     * Processes the go back.
     * @param event the action event that triggered the request
     */
    @FXML
    public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/devui/MiniGames.fxml", new MiniGamesController(gameEnvironment));
    }

    /**
     * Loads the selected minigame
     */
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