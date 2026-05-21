package seng201.team67.gui.instantiable.minigames;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng201.team67.models.minigames.MiniGameResult;

import java.util.function.Consumer;

/**
 * Controls the crowd wave view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class CrowdWaveController {

    /** FXML reference for the tap button control. */
    @FXML private Button tapButton;
    /** FXML reference for the result label control. */
    @FXML private Label resultLabel;

    /** Numeric value for the start time. */
    private long startTime;
    /** Numeric value for the beat interval. */
    private long beatInterval = 1000;
    /** Whether game ended. */
    private boolean gameEnded = false;

    /** Numeric value for the perfect count. */
    private int perfectCount = 0;
    /** Numeric value for the fail count. */
    private int failCount = 0;

    /** The on complete. */
    private Consumer<MiniGameResult> onComplete;

    private AnimationTimer timer;

    /**
     * Creates a new crowd wave controller.
     * @param onComplete the on complete
     */
    public CrowdWaveController(Consumer<MiniGameResult> onComplete) {
        this.onComplete = onComplete;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize() {
        tapButton.setText("Continue");
        resultLabel.setText("Get ready...");
        tapButton.setOnAction(e -> onStartClicked());
    }

    @FXML
    private void onStartClicked() {
        tapButton.setText("TAP");
        tapButton.setOnAction(e -> handleTap());

        startTime = System.currentTimeMillis();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameEnded) {
                    stop();
                    return;
                }

                long currentTime = System.currentTimeMillis();

                if ((currentTime - startTime) % beatInterval < 100) {
                    tapButton.setStyle("-fx-background-color: green;");
                } else {
                    tapButton.setStyle("");
                }
            }
        };
        timer.start();
    }

    @FXML
    private void handleTap() {
        if (gameEnded) return;

        long currentTime = System.currentTimeMillis();

        long beatNumber = (currentTime - startTime) / beatInterval;
        long expectedBeatTime = startTime + (beatNumber * beatInterval);

        long diff = Math.abs(currentTime - expectedBeatTime);

        if (diff < 150) {
            perfectCount++;
            resultLabel.setText("PERFECT (" + perfectCount + "/8)");
        } else if (diff < 300) {
            resultLabel.setText("GOOD");
        } else {
            failCount++;
            resultLabel.setText("MISS (" + failCount + "/4)");
        }

        if (failCount >= 4) {
            endGameLose();
            return;
        }

        if (perfectCount >= 8) {
            endGameWin();
        }
    }

    private void endGameWin() {
        gameEnded = true;

        resultLabel.setText("YOU NAILED THE CROWD!");

        MiniGameResult result = new MiniGameResult(30, 0);

        tapButton.setDisable(false);
        tapButton.setText("Continue");
        tapButton.setOnAction(e -> onComplete.accept(result));
    }

    private void endGameLose() {
        gameEnded = true;

        resultLabel.setText("YOU LOST THE RHYTHM...");

        MiniGameResult result = new MiniGameResult(-10, 0);

        tapButton.setDisable(false);
        tapButton.setText("Continue");
        tapButton.setOnAction(e -> onComplete.accept(result));
    }
}