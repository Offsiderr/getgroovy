package seng201.team67.gui.instantiable.minigames;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import seng201.team67.models.minigames.MiniGameResult;

import java.util.function.Consumer;

/**
 * Controls the crowd hype view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class CrowdHypeController {

    /** The hype bar. */
    @FXML private ProgressBar hypeBar;
    /** FXML reference for the result label control. */
    @FXML private Label resultLabel;
    /** FXML reference for the hype button control. */
    @FXML private Button hypeButton;
    /** FXML reference for the crowd image control. */
    @FXML private ImageView crowdImage;

    /** Numeric value for the hype. */
    private double hype = 0.5;
    /** The on complete. */
    private Consumer<MiniGameResult> onComplete;
    /** The timer. */
    private AnimationTimer timer;
    /** Whether game ended. */
    private boolean gameEnded = false;

    /**
     * Creates a new crowd hype controller.
     * @param onComplete the on complete
     */
    public CrowdHypeController(Consumer<MiniGameResult> onComplete) {
        this.onComplete = onComplete;
    }

    /**
     * Initialises the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize() {

        hypeButton.setText("Continue");
        resultLabel.setText("Get ready...");
        hypeButton.setOnAction(e -> onStartClicked());

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.6), hypeBar);
        pulse.setFromX(1);
        pulse.setToX(1.03);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(ScaleTransition.INDEFINITE);
        pulse.play();
    }

    @FXML
    private void onStartClicked() {
        hypeButton.setText("HYPE");
        hypeButton.setOnAction(e -> handleHype());

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameEnded) return;

                hype -= 0.004;

                if (hype <= 0) {
                    hype = 0;
                    gameEnded = true;
                    endGameLose();
                }

                if (hype >= 0.95) {
                    hype = 1;
                    gameEnded = true;
                    endGameWin();
                }

                hypeBar.setProgress(hype);
            }
        };
        timer.start();
    }

    @FXML
    private void handleHype() {
        hype += 0.05;
        if (hype > 1) {
            hype = 1;
        }
        hypeBar.setProgress(hype);

        TranslateTransition jump = new TranslateTransition(Duration.seconds(0.1), crowdImage);
        jump.setFromY(0);
        jump.setToY(-20);
        jump.setAutoReverse(true);
        jump.setCycleCount(2);
        jump.play();

        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.1), crowdImage);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void endGameWin() {
        timer.stop();

        resultLabel.setText("THE CROWD GOT TURNT!");
        resultLabel.setStyle("-fx-text-fill: black; -fx-font-size: 40px; -fx-effect: dropshadow(gaussian, #00ff00, 7, 0.6, 0, 0);");

        playPopAnimation();

        MiniGameResult result = new MiniGameResult(30, 0);

        hypeButton.setDisable(false);
        hypeButton.setText("Continue");
        hypeButton.setVisible(true);
        hypeButton.setOnAction(e -> onComplete.accept(result));
    }

    private void endGameLose() {
        timer.stop();

        resultLabel.setText("CROWD LOST HYPE...");
        resultLabel.setStyle("-fx-text-fill: black; -fx-font-size: 40px; -fx-effect: dropshadow(gaussian, #ff0000, 7, 0.6, 0, 0);");

        playPopAnimation();

        MiniGameResult result = new MiniGameResult(-10, 0);

        hypeButton.setDisable(false);
        hypeButton.setText("Continue");
        hypeButton.setVisible(true);
        hypeButton.setOnAction(e -> onComplete.accept(result));
    }

    private void playPopAnimation() {
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.2), resultLabel);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }
}