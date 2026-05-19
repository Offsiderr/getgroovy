package seng201.team67.gui.mainmenu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Slider;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ViewLoader;

public class MainSettingsController {

    private final GameEnvironment gameEnvironment;
    private final Pane settingsHolder;
    private final ViewLoader viewLoader = new ViewLoader();

    @FXML private Slider mainVolumeSlider;
    @FXML private Slider musicVolumeSlider;
    @FXML private Slider sfxVolumeSlider;
    @FXML private CheckBox fullscreen;

    public MainSettingsController(GameEnvironment gameEnvironment, Pane settingsHolder)
    {
        this.gameEnvironment = gameEnvironment;
        this.settingsHolder = settingsHolder;
    }

    @FXML public void initialize()
    {
        mainVolumeSlider.setValue(gameEnvironment.getConfig().mainVolume);
        musicVolumeSlider.setValue(gameEnvironment.getConfig().musicVolume);
        sfxVolumeSlider.setValue(gameEnvironment.getConfig().soundEffectsVolume);
        fullscreen.setSelected(gameEnvironment.getConfig().movingBackgroundEnabled);

        mainVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gameEnvironment.getConfig().mainVolume = newValue.doubleValue());
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gameEnvironment.getConfig().musicVolume = newValue.doubleValue());
        sfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gameEnvironment.getConfig().soundEffectsVolume = newValue.doubleValue());
        fullscreen.selectedProperty().addListener((observable, oldValue, newValue) ->
                gameEnvironment.getConfig().movingBackgroundEnabled = newValue);
    }

    @FXML public void goBack(ActionEvent event)
    {
        settingsHolder.getChildren().clear();
        settingsHolder.setDisable(true);
        settingsHolder.setVisible(false);
        settingsHolder.setManaged(false);
    }

    @FXML public void saveGame(ActionEvent event)
    {
        viewLoader.loadInto(settingsHolder, "/fxml/mainmenu/MainGameSave.fxml",
                new MainGameSaveController(gameEnvironment, settingsHolder));
    }

    @FXML public void quitGame(ActionEvent event)
    {
        Platform.exit();
    }

}
