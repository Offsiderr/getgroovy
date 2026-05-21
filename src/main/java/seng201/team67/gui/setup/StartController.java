package seng201.team67.gui.setup;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.services.audio.MusicService;
import seng201.team67.services.audio.SoundEffectsService;

import java.io.IOException;

/**
 * Controls the start view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class StartController {

    /** Service used to manage sound effects behaviour. */
    private final SoundEffectsService soundEffectsService;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();


    /** FXML reference for the start photo control. */
    @FXML private ImageView startPhoto;

    /**
     * Creates a new start controller.
     */
    public StartController()
    {
        GameEnvironment gameEnvironment = new GameEnvironment();
        this.soundEffectsService = new SoundEffectsService(gameEnvironment); //Fix to not rely on game enviroment
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML public void initialize()
    {
        ScaleTransition zoomIn = new ScaleTransition(Duration.seconds(6), startPhoto);
        zoomIn.setFromX(1.0);
        zoomIn.setFromY(1.0);
        zoomIn.setToX(1.08);
        zoomIn.setToY(1.08);
        zoomIn.setCycleCount(Animation.INDEFINITE);
        zoomIn.setAutoReverse(true);
        zoomIn.play();
        MusicService.play("/sound/Music/Title_Screen_Placeholder.wav", 15.0);
    }

    /**
     * Processes starting the game and bringing the player to the setup screen
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void onStartGame(ActionEvent event) throws IOException {
        soundEffectsService.playYes();

        Node currentRoot = ((Node) event.getSource()).getScene().getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            screenNavigator.navigate(event, "/fxml/setup/PlayerSaveSelection.fxml", new SelectSaveController());
        });

        fadeOut.play();
    }

}