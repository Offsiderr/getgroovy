package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.services.SoundEffectsService;

import java.io.IOException;

public class SetupController {
    //PlayerSelections.fxml logic

    @FXML private TextField labelNameField;
    @FXML private Spinner<Integer> expeditionCountSpinner;
    @FXML private ToggleGroup difficultyGroup; //Difficulty toggles are grouped together in a toggle group in SceneBuilder
    @FXML private javafx.scene.control.Button startButton;

    public final GameEnvironment gameEnvironment;
    private SoundEffectsService soundEffectsService  = new SoundEffectsService();

    private Stage stage;
    private Scene scene;
    private Parent root;

    public SetupController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
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
        startButton.setDisable(!isFormValid());
    }

    private boolean isFormValid() {
        String name = labelNameField.getText();
        boolean nameValid = name != null
                && name.length() >= gameEnvironment.getConfig().labelNameMinLength
                && name.length() <= gameEnvironment.getConfig().labelNameMaxLength
                && name.matches("[a-zA-Z0-9 ]+");

        boolean difficultyValid = difficultyGroup.getSelectedToggle() != null;

        return nameValid && difficultyValid;
    }

    @FXML
    public void onStartGame(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        //setting up the game enviroment
        gameEnvironment.setLabelName(labelNameField.getText());
        gameEnvironment.setDifficulty(difficultyGroup.getToggles().indexOf(difficultyGroup.getSelectedToggle()));
        gameEnvironment.setSelectedNumTours(expeditionCountSpinner.getValue());

        //Now let's load the artist selection scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartingArtistSelection.fxml"));
        loader.setController(new ArtistSelectionController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
