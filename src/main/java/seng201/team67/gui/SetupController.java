package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.services.audio.SoundEffectsService;
import seng201.team67.services.setup.DifficultyService;
import seng201.team67.services.setup.SetupService;

import java.io.IOException;

public class SetupController {
    //PlayerSelections.fxml logic

    @FXML private TextField labelNameField;
    @FXML private Spinner<Integer> expeditionCountSpinner;
    @FXML private ToggleGroup difficultyGroup; //Difficulty toggles are grouped together in a toggle group in SceneBuilder
    @FXML private javafx.scene.control.Button startButton;

    public final GameEnvironment gameEnvironment;
    private final SetupService setupService;
    private final DifficultyService difficultyService;
    private SoundEffectsService soundEffectsService;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    public SetupController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        setupService = new SetupService(gameEnvironment);
        difficultyService = new DifficultyService();
        soundEffectsService = new SoundEffectsService(gameEnvironment);
    }

    public void handleNext(ActionEvent event) throws IOException {

    }

    @FXML
    public void initialize()
    {

        //Is disabled anyway in the scene builder but doesn't hurt to make sure it is
        startButton.setDisable(true);

        //Set spinner values
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(gameEnvironment.getConfig().minTourCount, gameEnvironment.getConfig().maxTourCount, gameEnvironment.getConfig().defaultTourCount);
        expeditionCountSpinner.setValueFactory(valueFactory);

        // Revalidate whenever name changes
        labelNameField.textProperty().addListener(
                (obs, oldVal, newVal) -> validateForm()
        );

        // Revalidate whenever difficulty changes
        difficultyGroup.selectedToggleProperty().addListener(
                (obs, oldVal, newVal) -> validateForm()
        );
    }

    private void validateForm() {
        startButton.setDisable(!setupService.isFormValid(labelNameField, difficultyGroup));
    }


    @FXML
    public void onStartGame(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        //setting up the game enviroment
        gameEnvironment.setLabelName(labelNameField.getText());
        difficultyService.applyDifficulty(gameEnvironment, difficultyGroup.getToggles().indexOf(difficultyGroup.getSelectedToggle()));
        gameEnvironment.setSelectedNumTours(expeditionCountSpinner.getValue());

        screenNavigator.navigate(event, "/fxml/StartingArtistSelection.fxml", new ArtistSelectionController(gameEnvironment));
    }
}
