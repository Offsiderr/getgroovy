package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class MainController {

    @FXML
    private Label defaultLabel;

    @FXML
    private Button defaultButton;

    @FXML
    private Label nameDisplay;

    private String guildName = "";
    private boolean cursorVisible = true;
    private Timeline cursorBlink;

    public void init(Stage stage) {
        cursorBlink = new Timeline(
                new KeyFrame(Duration.millis(500), e -> {
                    cursorVisible = !cursorVisible;
                    updateDisplay();
                })
        );
        cursorBlink.setCycleCount(Timeline.INDEFINITE);
        cursorBlink.play();
    }

    private void updateDisplay() {
        nameDisplay.setText(cursorVisible ? guildName + "_" : guildName);
    }

    @FXML
    private void handleKeyPress(KeyEvent e) {
        if (e.getCode() == KeyCode.BACK_SPACE) {
            if (!guildName.isEmpty())
                guildName = guildName.substring(0, guildName.length() - 1);
        } else if (e.getText() != null && !e.getText().isEmpty()) {
            if (guildName.length() < 15)
                guildName += e.getText().toUpperCase();
        }
        updateDisplay();
    }

    @FXML
    public void onButtonClicked() {
    }

}