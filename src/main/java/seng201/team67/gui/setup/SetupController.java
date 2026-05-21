package seng201.team67.gui.setup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.selection.ArtistSelectionController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.services.audio.SoundEffectsService;
import seng201.team67.services.setup.DifficultyService;
import seng201.team67.services.setup.SetupService;

import java.io.IOException;

/**
 * Controls the setup view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class  SetupController {
    //PlayerSelections.fxml logic

    /** FXML reference for the label name field control. */
    @FXML private TextField labelNameField;
    /** The expedition count spinner. */
    @FXML private Spinner<Integer> expeditionCountSpinner;
    /** The difficulty group. */
    @FXML private ToggleGroup difficultyGroup; //Difficulty toggles are grouped together in a toggle group in SceneBuilder
    /** The start button. */
    @FXML private javafx.scene.control.Button startButton;

    /** Shared game state for the current session. */
    public final GameEnvironment gameEnvironment;
    /** Service used to manage setup behaviour. */
    private final SetupService setupService;
    /** Service used to manage difficulty behaviour. */
    private final DifficultyService difficultyService;
    /** Service used to manage sound effects behaviour. */
    private SoundEffectsService soundEffectsService;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    /**
     * Creates a new setup controller.
     * @param gameEnvironment the active game environment
     */
    public SetupController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        setupService = new SetupService(gameEnvironment);
        difficultyService = new DifficultyService();
        soundEffectsService = new SoundEffectsService(gameEnvironment);
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
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
        startButton.setDisable(!setupService.isFormValid(
                labelNameField.getText(),
                difficultyGroup.getSelectedToggle() != null
        ));
    }


    /**
     * Starts the game. Sets the starting settings in the game enviroment
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void onStartGame(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        //setting up the game enviroment
        gameEnvironment.setLabelName(labelNameField.getText());
        difficultyService.applyDifficulty(gameEnvironment, difficultyGroup.getToggles().indexOf(difficultyGroup.getSelectedToggle()));
        gameEnvironment.setSelectedNumTours(expeditionCountSpinner.getValue());

        screenNavigator.navigate(event, "/fxml/setup/StartingArtistSelection.fxml", new ArtistSelectionController(gameEnvironment));
    }
}
