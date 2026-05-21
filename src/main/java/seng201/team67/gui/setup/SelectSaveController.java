package seng201.team67.gui.setup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.services.data.GameSaveService;

/**
 * Controls the select save view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class SelectSaveController {

    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** Service used to manage game save behaviour. */
    private final GameSaveService gameSaveService = new GameSaveService();

    /** FXML reference for the anchor pane control. */
    @FXML private AnchorPane anchorPane;
    /** FXML reference for the save button 1 control. */
    @FXML private Button saveButton1;
    /** FXML reference for the save button 2 control. */
    @FXML private Button saveButton2;
    /** FXML reference for the save button 3 control. */
    @FXML private Button saveButton3;

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize()
    {
        configureSaveButton(saveButton1, 1);
        configureSaveButton(saveButton2, 2);
        configureSaveButton(saveButton3, 3);
    }

    /**
     * Processes opening the first save
     * @param event the action event that triggered the request
     */
    @FXML
    public void onSave1(ActionEvent event)
    {
        openSave(event, 1);
    }

    /**
     * Processes opening the second save
     * @param event the action event that triggered the request
     */
    @FXML
    public void onSave2(ActionEvent event)
    {
        openSave(event, 2);
    }

    /**
     * Processes opening the third save
     * @param event the action event that triggered the request
     */
    @FXML
    public void onSave3(ActionEvent event)
    {
        openSave(event, 3);
    }

    /**
     * Processes starting a new game
     */
    @FXML
    public void onStartGame()
    {
        screenNavigator.navigate(anchorPane, "/fxml/setup/PlayerSelections.fxml", new SetupController(new GameEnvironment()));
    }

    private void openSave(ActionEvent event, int slot)
    {
        if (!gameSaveService.hasSave(slot)) {
            return;
        }

        GameEnvironment gameEnvironment = gameSaveService.loadGame(slot);
        if (gameEnvironment == null) {
            return;
        }
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

    /**
     * Pass in the button and the slot to check if a save exists for that slots, and if so,
     * then the game changes the text to the label name and number of expeditions
     * @param button the save button to change
     * @param slot the save slot to check
     */
    private void configureSaveButton(Button button, int slot)
    {
        if (!gameSaveService.hasSave(slot)) {
            button.setText("Empty Slot");
            return;
        }

        GameEnvironment gameEnvironment = gameSaveService.loadGame(slot);
        if (gameEnvironment == null || gameEnvironment.getLabel() == null) {
            button.setText("Empty Slot");
            return;
        }

        String labelName = "Unknown Label";
        String expeditionText = "Expeditions: 0/0";

        if (gameEnvironment.getLabel().getName() != null) {
            labelName = gameEnvironment.getLabel().getName();
        }
        expeditionText = "Expeditions: " + gameEnvironment.getTourCount() + "/" + gameEnvironment.getSelectedNumTours();

        button.setText(labelName + "\n" + expeditionText);
    }
}
