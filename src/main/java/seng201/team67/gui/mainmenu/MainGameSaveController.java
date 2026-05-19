package seng201.team67.gui.mainmenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import seng201.team67.GameEnvironment;
import seng201.team67.services.data.GameSaveService;

public class MainGameSaveController {

    private final GameEnvironment gameEnvironment;
    private final Pane settingsHolder;
    private final GameSaveService gameSaveService = new GameSaveService();
    private int selectedSaveSlot = 1;

    @FXML private Button startButton;
    @FXML private Button startButton1;
    @FXML private Button startButton11;
    @FXML private Button startButton111;

    public MainGameSaveController(GameEnvironment gameEnvironment, Pane settingsHolder) {
        this.gameEnvironment = gameEnvironment;
        this.settingsHolder = settingsHolder;
    }

    @FXML
    public void initialize() {
        refreshSaveSlotTexts();
        updateSelectedButtonStyle();
        refreshSaveButtonText();
    }

    @FXML
    public void onSave1(ActionEvent event) {
        selectedSaveSlot = 1;
        updateSelectedButtonStyle();
        refreshSaveButtonText();
    }

    @FXML
    public void onSave2(ActionEvent event) {
        selectedSaveSlot = 2;
        updateSelectedButtonStyle();
        refreshSaveButtonText();
    }

    @FXML
    public void onSave3(ActionEvent event) {
        selectedSaveSlot = 3;
        updateSelectedButtonStyle();
        refreshSaveButtonText();
    }

    @FXML
    public void onStartGame(ActionEvent event) {
        gameSaveService.saveGame(gameEnvironment, selectedSaveSlot);
        settingsHolder.getChildren().clear();
        settingsHolder.setDisable(true);
        settingsHolder.setVisible(false);
        settingsHolder.setManaged(false);
    }

    private void refreshSaveButtonText() {
        if (startButton != null) {
            startButton.setText("Save Game (Slot " + selectedSaveSlot + ")");
        }
    }

    private void refreshSaveSlotTexts() {
        setSaveSlotText(startButton1, 1);
        setSaveSlotText(startButton11, 2);
        setSaveSlotText(startButton111, 3);
    }

    private void setSaveSlotText(Button button, int slot) {
        if (button == null) {
            return;
        }

        if (!gameSaveService.hasSave(slot)) {
            button.setText("Empty Slot");
            return;
        }

        GameEnvironment savedGame = gameSaveService.loadGame(slot);
        if (savedGame == null || savedGame.getLabel() == null) {
            button.setText("Empty Slot");
            return;
        }

        button.setText(savedGame.getLabel().getName() + "\nExpeditions: "
                + savedGame.getTourCount() + "/" + savedGame.getSelectedNumTours());
    }

    private void updateSelectedButtonStyle() {
        applySlotStyle(startButton1, selectedSaveSlot == 1);
        applySlotStyle(startButton11, selectedSaveSlot == 2);
        applySlotStyle(startButton111, selectedSaveSlot == 3);
    }

    private void applySlotStyle(Button button, boolean selected) {
        if (button == null) {
            return;
        }

        if (selected) {
            button.setStyle("-fx-border-color: #0078d7; -fx-border-width: 3; -fx-background-color: #dce9f7;");
        } else {
            button.setStyle("");
        }
    }
}
