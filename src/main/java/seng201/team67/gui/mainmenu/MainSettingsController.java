package seng201.team67.gui.mainmenu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Slider;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ViewLoader;

/**
 * Controls the main settings view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MainSettingsController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** FXML reference for the settings holder control. */
    private final Pane settingsHolder;
    /** The view loader. */
    private final ViewLoader viewLoader = new ViewLoader();

    /** The main volume slider. */
    @FXML private Slider mainVolumeSlider;
    /** The music volume slider. */
    @FXML private Slider musicVolumeSlider;
    /** The sfx volume slider. */
    @FXML private Slider sfxVolumeSlider;
    /** The fullscreen. */
    @FXML private CheckBox fullscreen;

    /**
     * Creates a new main settings controller.
     * @param gameEnvironment the active game environment
     * @param settingsHolder the settings holder
     */
    public MainSettingsController(GameEnvironment gameEnvironment, Pane settingsHolder)
    {
        this.gameEnvironment = gameEnvironment;
        this.settingsHolder = settingsHolder;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML public void initialize()
    {
        mainVolumeSlider.setValue(gameEnvironment.getConfig().mainVolume);
        musicVolumeSlider.setValue(gameEnvironment.getConfig().musicVolume);
        sfxVolumeSlider.setValue(gameEnvironment.getConfig().soundEffectsVolume);
        fullscreen.setSelected(gameEnvironment.getConfig().movingBackgroundEnabled);

        //Add listeners so that the volume slider updates the volume when moved.
        mainVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gameEnvironment.getConfig().mainVolume = newValue.doubleValue());
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gameEnvironment.getConfig().musicVolume = newValue.doubleValue());
        sfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gameEnvironment.getConfig().soundEffectsVolume = newValue.doubleValue());
        fullscreen.selectedProperty().addListener((observable, oldValue, newValue) ->
                gameEnvironment.getConfig().movingBackgroundEnabled = newValue);
    }

    /**
     * Takes the player to the main menu
     * @param event the action event that triggered the request
     */
    @FXML public void goBack(ActionEvent event)
    {
        settingsHolder.getChildren().clear();
        settingsHolder.setDisable(true);
        settingsHolder.setVisible(false);
        settingsHolder.setManaged(false);
    }

    /**
     * Opens the save game menu
     * @param event the action event that triggered the request
     */
    @FXML public void saveGame(ActionEvent event)
    {
        viewLoader.loadInto(settingsHolder, "/fxml/mainmenu/MainGameSave.fxml",
                new MainGameSaveController(gameEnvironment, settingsHolder));
    }

    /**
     * Quits the game
     * @param event the action event that triggered the request
     */
    @FXML public void quitGame(ActionEvent event)
    {
        Platform.exit();
    }

}
