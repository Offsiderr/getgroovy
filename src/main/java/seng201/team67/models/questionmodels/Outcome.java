package seng201.team67.models.questionmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Outcome {

    private final int weight;
    private final String description;
    private final int creditChange;

    private final int staminaChange; //TODO: stamina needs to be implemented properly with artists.
    private final int crowdEnergyChange;
    private final boolean expeditionEnds;


    @JsonCreator
    public Outcome(
            @JsonProperty("weight") int weight,
            @JsonProperty("description") String description,
            @JsonProperty("creditChange") int creditChange,
            @JsonProperty("staminaChange") int staminaChange,
            @JsonProperty("crowdEnergyChange") int crowdEnergyChange,
            @JsonProperty("expeditionEnds") boolean expeditionEnds) {
        this.weight = weight;
        this.description = description;
        this.creditChange = creditChange;
        this.staminaChange = staminaChange;
        this.crowdEnergyChange = crowdEnergyChange;
        this.expeditionEnds = expeditionEnds;
    }


}
