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
import seng201.team67.services.audio.SoundEffectsService;

import java.io.IOException;

public class StartController {

    private final SoundEffectsService soundEffectsService;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    private final GameEnvironment gameEnvironment;

    @FXML private ImageView startPhoto;

    public StartController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        this.soundEffectsService = new SoundEffectsService(gameEnvironment);
    }

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
    }

    @FXML
    public void onStartGame(ActionEvent event) throws IOException {
        soundEffectsService.playYes();

        Node currentRoot = ((Node) event.getSource()).getScene().getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            screenNavigator.navigate(event, "/fxml/setup/PlayerSelections.fxml", new SetupController(gameEnvironment));
        });

        fadeOut.play();
    }

}