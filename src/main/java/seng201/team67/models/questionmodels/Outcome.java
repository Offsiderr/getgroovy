package seng201.team67.models.questionmodels;

public class Outcome {

    private final int weight;
    private final String description;
    private final int creditChange;

    private final int staminaChange; //TODO: stamina needs to be implemented properly with artists.
    private final int crowdEnergyChange;
    private final boolean expeditionEnds;


    public Outcome(int weight, String description, int creditChange, int staminaChange, int crowdEnergyChange, boolean expeditionEnds)
    {
        this.weight = weight;
        this.description = description;
        this.creditChange = creditChange;
        this.staminaChange = staminaChange;
        this.crowdEnergyChange = crowdEnergyChange;
        this.expeditionEnds = expeditionEnds;
    }
}
