package seng201.team67.models.questionmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.PayoutTier;
import seng201.team67.models.enums.PayoutType;

public class Outcome {

    private final int weight;
    private final String description;
    private final PayoutType payoutType;

    private final int staminaChange; //TODO: stamina needs to be implemented properly with artists.
    private final int crowdEnergyChange;
    private final boolean concertEnds;


    @JsonCreator
    public Outcome(
            @JsonProperty("weight") int weight,
            @JsonProperty("description") String description,
            @JsonProperty("payout") PayoutType payoutType,
            @JsonProperty("staminaChange") int staminaChange,
            @JsonProperty("crowdEnergyChange") int crowdEnergyChange,
            @JsonProperty("concertEnds") boolean concertEnds) {
        this.weight = weight;
        this.description = description;
        this.payoutType = payoutType;
        this.staminaChange = staminaChange;
        this.crowdEnergyChange = crowdEnergyChange;
        this.concertEnds = concertEnds;
    }

    //the many getters

    public int getWeight() {
        return weight;
    }

    public String getDescription()
    {
        return description;
    }

    public PayoutType getPayoutType(){return payoutType;}

    public int getStaminaChange()
    {
        return staminaChange;
    }

    public int getCrowdEnergyChange()
    {
        return crowdEnergyChange;
    }

    public boolean getConcertEnds()
    {
        return concertEnds;
    }
}
