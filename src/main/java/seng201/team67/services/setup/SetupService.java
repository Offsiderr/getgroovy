package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;

/**
 * Provides setup operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class SetupService {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;

    /**
     * Creates a new setup service.
     * @param gameEnvironment the active game environment
     */
    public SetupService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Returns whether the setup selections are valid. (Name is correct, difficulty is selected)
     * @param labelName the text value for the label name
     * @param difficultySelected whether difficulty selected
     * @return True if form valid, otherwise false.
     */
    public boolean isFormValid(String labelName, boolean difficultySelected)
    {
        return isLabelNameValid(labelName) && difficultySelected;
    }

    /**
     * Returns whether the label name valid.
     * @param labelName the text value for the label name
     * @return True if label name valid, otherwise false.
     */
    public boolean isLabelNameValid(String labelName)
    {
        String normalizedName = normalizeLabelName(labelName);
        return normalizedName != null
                && normalizedName.length() >= gameEnvironment.getConfig().labelNameMinLength
                && normalizedName.length() <= gameEnvironment.getConfig().labelNameMaxLength
                && normalizedName.matches("[a-zA-Z0-9 ]+");
    }

    /**
     * Checks if the label name is normalised.
     * @param labelName the text value for the label name
     * @return The require valid label name.
     */
    public String requireValidLabelName(String labelName)
    {
        String normalizedName = normalizeLabelName(labelName);
        if (!isLabelNameValid(normalizedName))
        {
            throw new IllegalArgumentException("Label name must be 3-15 alphanumeric characters and spaces only.");
        }
        return normalizedName;
    }

    private String normalizeLabelName(String labelName)
    {
        if (labelName == null)
        {
            return null;
        }
        return labelName.trim();
    }
}
