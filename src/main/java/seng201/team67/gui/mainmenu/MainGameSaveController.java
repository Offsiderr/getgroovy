package seng201.team67.gui.mainmenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import seng201.team67.GameEnvironment;
import seng201.team67.services.data.GameSaveService;

/**
 * Controls the main game save view and coordinates its user interactions.
 * This handles saving the game from the settings menu
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MainGameSaveController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** FXML reference for the settings holder control. */
    private final Pane settingsHolder;
    /** Service used to manage game save behaviour. */
    private final GameSaveService gameSaveService = new GameSaveService();
    /** Numeric value for the selected save slot. */
    private int selectedSaveSlot = 1;

    /** FXML reference for the first save control. */
    @FXML private Button saveButton1;
    /** FXML reference for the second save control. */
    @FXML private Button saveButton2;
    /** FXML reference for the third save control. */
    @FXML private Button saveButton3;
    /** FXML reference for the new game control. */
    @FXML private Button newGame;

    /**
     * Creates a new main game save controller.
     * @param gameEnvironment the active game environment
     * @param settingsHolder the settings holder
     */
    public MainGameSaveController(GameEnvironment gameEnvironment, Pane settingsHolder) {
        this.gameEnvironment = gameEnvironment;
        this.settingsHolder = settingsHolder;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize() {
        refreshSaveSlotTexts();
        updateSelectedButtonStyle();
        refreshSaveButtonText();
    }

    /**
     * Processes the player selecting the first save.
     * @param event the action event that triggered the request
     */
    @FXML
    public void onSave1(ActionEvent event) {
        selectedSaveSlot = 1;
        updateSelectedButtonStyle();
        refreshSaveButtonText();
    }

    /**
     * Processes the player selecting the second save.
     * @param event the action event that triggered the request
     */
    @FXML
    public void onSave2(ActionEvent event) {
        selectedSaveSlot = 2;
        updateSelectedButtonStyle();
        refreshSaveButtonText();
    }

    /**
     * Processes the player selecting the third save.
     * @param event the action event that triggered the request
     */
    @FXML
    public void onSave3(ActionEvent event) {
        selectedSaveSlot = 3;
        updateSelectedButtonStyle();
        refreshSaveButtonText();
    }

    /**
     * Processes the player selecting to start a new game
     * @param event the action event that triggered the request
     */
    @FXML
    public void onStartGame(ActionEvent event) {
        gameSaveService.saveGame(gameEnvironment, selectedSaveSlot);
        settingsHolder.getChildren().clear();
        settingsHolder.setDisable(true);
        settingsHolder.setVisible(false);
        settingsHolder.setManaged(false);
    }

    /**
     * If you select a save, then the save game button is adjusted accordingly in this method
     */
    private void refreshSaveButtonText() {
        if (newGame != null) {
            newGame.setText("Save Game (Slot " + selectedSaveSlot + ")");
        }
    }

    private void refreshSaveSlotTexts() {
        setSaveSlotText(saveButton1, 1);
        setSaveSlotText(saveButton2, 2);
        setSaveSlotText(saveButton3, 3);
    }

    /**
     * Pass in the button and the slot to check if a save exists for that slots, and if so,
     * then the game changes the text to the label name and number of expeditions
     * @param button the save button to change
     * @param slot the save slot to check
     */
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
        applySlotStyle(saveButton1, selectedSaveSlot == 1);
        applySlotStyle(saveButton2, selectedSaveSlot == 2);
        applySlotStyle(saveButton3, selectedSaveSlot == 3);
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
