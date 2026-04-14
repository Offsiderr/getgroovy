package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.*;

import java.awt.*;
import java.io.IOException;

public class SetupController {
    //PlayerSelections.fxml logic

    @FXML private TextField labelNameField;
    @FXML private Spinner<Integer> expeditionCountSpinner;
    @FXML private ToggleGroup difficultyGroup; //Difficulty toggles are grouped together in a toggle group in SceneBuilder
    @FXML private javafx.scene.control.Button startButton;

    public void handleNext(ActionEvent event) throws IOException {

    }

    @FXML
    public void initialize()
    {
        //Is disabled anyway in the scene builder but doesn't hurt to make sure it is
        startButton.setDisable(true);

        //Set spinner values
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 15, 5);
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

    @FXML
    public void startButton()
    {

    }

    private void validateForm() {
        startButton.setDisable(!isFormValid());
    }

    private boolean isFormValid() {
        String name = labelNameField.getText();
        boolean nameValid = name != null
                && name.length() >= 3
                && name.length() <= 15
                && name.matches("[a-zA-Z0-9 ]+");

        boolean difficultyValid = difficultyGroup.getSelectedToggle() != null;

        return nameValid && difficultyValid;
    }
}
