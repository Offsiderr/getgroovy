package seng201.team67.gui.instantiable.minigames;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import seng201.team67.models.minigames.MiniGameResult;

import java.util.function.Consumer;

public class MicTimingController {

    @FXML private Slider timingSlider;
    @FXML private Label resultLabel;
    @FXML private Button hitButton;

    @FXML private Label missLeft;
    @FXML private Label goodLeft;
    @FXML private Label perfect;
    @FXML private Label goodRight;
    @FXML private Label missRight;

    @FXML private ImageView micThumb;

    private double direction = 1;
    private Consumer<MiniGameResult> gameResult;
    private AnimationTimer timer;

    public MicTimingController(Consumer<MiniGameResult> gameResult) {
        this.gameResult = gameResult;
    }

    @FXML
    public void initialize() {
        timingSlider.setMin(0);
        timingSlider.setMax(100);

        micThumb.setImage(new Image(getClass().getResource("/images/GoldMic.png").toExternalForm()));

        positionMarkers();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double value = timingSlider.getValue();
                value += direction * 1.5;

                if (value >= 100 || value <= 0) {
                    direction *= -1;
                }

                timingSlider.setValue(value);

                updateMicPosition();
            }
        };
        timer.start();
    }

    private void updateMicPosition() {
        double width = timingSlider.getPrefWidth();
        double startX = timingSlider.getLayoutX();
        double y = timingSlider.getLayoutY();

        double padding = 20;
        double usableWidth = width - (padding * 2);

        double x = startX + padding + (timingSlider.getValue() / 100.0) * usableWidth;

        micThumb.setLayoutX(x - 20);
        micThumb.setLayoutY(y - 20);
    }

    private void positionMarkers() {
        double width = timingSlider.getPrefWidth();
        double startX = timingSlider.getLayoutX();
        double y = timingSlider.getLayoutY() - 20;

        placeMarker(missLeft, 0, width, startX, y);
        placeMarker(goodLeft, 35, width, startX, y);
        placeMarker(perfect, 50, width, startX, y);
        placeMarker(goodRight, 65, width, startX, y);
        placeMarker(missRight, 100, width, startX, y);

        missLeft.setStyle("-fx-text-fill: red; -fx-effect: dropshadow(gaussian, red, 8, 0.6, 0, 0);");
        goodLeft.setStyle("-fx-text-fill: orange; -fx-effect: dropshadow(gaussian, orange, 8, 0.6, 0, 0);");
        perfect.setStyle("-fx-text-fill: lime; -fx-effect: dropshadow(gaussian, lime, 10, 0.7, 0, 0);");
        goodRight.setStyle("-fx-text-fill: orange; -fx-effect: dropshadow(gaussian, orange, 8, 0.6, 0, 0);");
        missRight.setStyle("-fx-text-fill: red; -fx-effect: dropshadow(gaussian, red, 8, 0.6, 0, 0);");
    }

    private void placeMarker(Label label, double value, double width, double startX, double y) {
        double padding = 20;
        double usableWidth = width - (padding * 2);

        double x = startX + padding + (value / 100.0) * usableWidth;

        label.setLayoutX(x);
        label.setLayoutY(y);
    }

    @FXML
    private void handleHit() {
        timer.stop();
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