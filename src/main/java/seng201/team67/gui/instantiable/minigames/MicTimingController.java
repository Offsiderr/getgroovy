package seng201.team67.gui.instantiable.minigames;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import seng201.team67.models.minigames.MiniGameResult;

import java.util.function.Consumer;

public class MicTimingController {

    @FXML private Slider timingSlider;
    @FXML private Label resultLabel;
    @FXML private Button hitButton;

    private double direction = 1;
    private Consumer<MiniGameResult> gameResult;

    public MicTimingController(Consumer<MiniGameResult> gameResult) {
        this.gameResult = gameResult;
    }

    @FXML
    public void initialize() {
        timingSlider.setMin(0);
        timingSlider.setMax(100);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double value = timingSlider.getValue();
                value += direction * 1.5;

                if (value >= 100 || value <= 0) {
                    direction *= -1;
                }

                timingSlider.setValue(value);
            }
        };
        timer.start();
    }

    @FXML
    private void handleHit() {
        double value = timingSlider.getValue();
        double distance = Math.abs(value - 50);

        MiniGameResult result;

        if (distance < 5) {
            resultLabel.setText("PERFECT!!!");
            resultLabel.setStyle("-fx-text-fill: black; -fx-background-color: transparent; -fx-padding: 0; -fx-effect: dropshadow(gaussian, #00ffcc, 3, 0.3, 0, 0);");
            playPopAnimation();
            result = new MiniGameResult(30, 0);
        } else if (distance < 15) {
            resultLabel.setText("GOOD!");
            resultLabel.setStyle("-fx-text-fill: black; -fx-background-color: transparent; -fx-padding: 0; -fx-effect: dropshadow(gaussian, #00ff00, 3, 0.3, 0, 0);");
            playPopAnimation();
            result = new MiniGameResult(10, 0);
        } else {
            resultLabel.setText("MISS!");
            resultLabel.setStyle("-fx-text-fill: black; -fx-background-color: transparent; -fx-padding: 0; -fx-effect: dropshadow(gaussian, #ff0000, 3, 0.3, 0, 0);");
            playPopAnimation();
            result = new MiniGameResult(-10, 0);
        }

        resultLabel.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        resultLabel.setMaxSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

        hitButton.setDisable(true);

        hitButton.setOnAction(e -> gameResult.accept(result));
        hitButton.setText("Continue");
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