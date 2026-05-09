package seng201.team67.services.setup;

import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import seng201.team67.GameEnvironment;

import java.awt.*;

public class SetupService {

    private GameEnvironment gameEnvironment;

    public SetupService(GameEnvironment gameEnviroment)
    {
        this.gameEnvironment = gameEnviroment;
    }

    public boolean isFormValid(TextField labelNameField, ToggleGroup difficultyGroup) {
        String name = labelNameField.getText();
        boolean nameValid = name != null
                && name.length() >= gameEnvironment.getConfig().labelNameMinLength
                && name.length() <= gameEnvironment.getConfig().labelNameMaxLength
                && name.matches("[a-zA-Z0-9 ]+");

        boolean difficultyValid = difficultyGroup.getSelectedToggle() != null;

        return nameValid && difficultyValid;
    }
}
