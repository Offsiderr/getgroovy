package seng201.team67.models.questionmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.questions.OutcomeType;
import seng201.team67.models.enums.questions.PayoutType;

public class Outcome {

    private final int weight;
    private final String description;
    private final OutcomeType outcomeType;
    private final PayoutType payoutType;
    private final Double staminaChange;
    private final int crowdEnergyChange;
    private final boolean concertEnds;


    @JsonCreator
    public Outcome(
            @JsonProperty("weight") int weight,
            @JsonProperty("description") String description,
            @JsonProperty("outcomeType") OutcomeType outcomeType,
            @JsonProperty("payout") PayoutType payoutType,
            @JsonProperty("staminaChange") Double staminaChange,
            @JsonProperty("crowdEnergyChange") int crowdEnergyChange,
            @JsonProperty("concertEnds") boolean concertEnds) {
        this.weight = weight;
        this.description = description;
        this.outcomeType = outcomeType == null ? inferOutcomeType(payoutType) : outcomeType;
        this.payoutType = payoutType;
        this.staminaChange = staminaChange;
        this.crowdEnergyChange = crowdEnergyChange;
        this.concertEnds = concertEnds;
    }

    public Outcome(int weight, String description, PayoutType payoutType, double staminaChange,
                   int crowdEnergyChange, boolean concertEnds)
    {
        this(weight, description, inferOutcomeType(payoutType), payoutType, staminaChange, crowdEnergyChange, concertEnds);
    }

    private static OutcomeType inferOutcomeType(PayoutType payoutType)
    {
        return switch (payoutType) {
            case MAJOR_REWARD -> OutcomeType.GREAT;
            case MINOR_REWARD -> OutcomeType.OK;
            case MINOR_PENALTY -> OutcomeType.BAD;
            case MAJOR_PENALTY -> OutcomeType.TERRIBLE;
            case NONE -> OutcomeType.NONE;
        };
    }

    //the many getters

    public int getWeight() {
        return weight;
    }

    public String getDescription()
    {
        return description;
    }

    public OutcomeType getOutcomeType()
    {
        return outcomeType;
    }

    public PayoutType getPayoutType(){return payoutType;}

    public double getStaminaChange()
    {
        return staminaChange == null ? 0.0 : staminaChange;
    }

    public boolean hasExplicitStaminaChange()
    {
        return staminaChange != null;
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
