package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;

public class SetupService {

    private final GameEnvironment gameEnvironment;

    public SetupService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    public boolean isFormValid(String labelName, boolean difficultySelected)
    {
        return isLabelNameValid(labelName) && difficultySelected;
    }

    public boolean isLabelNameValid(String labelName)
    {
        String normalizedName = normalizeLabelName(labelName);
        return normalizedName != null
                && normalizedName.length() >= gameEnvironment.getConfig().labelNameMinLength
                && normalizedName.length() <= gameEnvironment.getConfig().labelNameMaxLength
                && normalizedName.matches("[a-zA-Z0-9 ]+");
    }

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
