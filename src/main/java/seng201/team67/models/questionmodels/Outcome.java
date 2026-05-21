package seng201.team67.models.questionmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.questions.OutcomeType;
import seng201.team67.models.enums.questions.PayoutType;

/**
 * Represents the outcome used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class Outcome {

    /** Numeric value for the weight. */
    private final int weight;
    /** Text value for the description. */
    private final String description;
    /** The outcome type. */
    private final OutcomeType outcomeType;
    /** The payout type. */
    private final PayoutType payoutType;
    /** Numeric value for the stamina change. */
    private final Double staminaChange;
    /** Numeric value for the crowd energy change. */
    private final int crowdEnergyChange;
    /** Whether concert ends. */
    private final boolean concertEnds;


    /**
     * Creates a new outcome.
     * It initializes the state needed for the surrounding game flow.
     * @param weight the numeric value for the weight
     * @param description the description text to use
     * @param outcomeType the outcome type
     * @param payoutType the payout type
     * @param staminaChange the numeric value for the stamina change
     * @param crowdEnergyChange the numeric value for the crowd energy change
     * @param concertEnds whether concert ends
     */
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

    /**
     * Creates a new outcome.
     * @param weight the numeric value for the weight
     * @param description the description text to use
     * @param payoutType the payout type
     * @param staminaChange the numeric value for the stamina change
     * @param crowdEnergyChange the numeric value for the crowd energy change
     * @param concertEnds whether concert ends
     */
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

    /**
     * Returns the weight.
     * @return The weight.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns the description.
     * @return The description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the outcome type.
     * @return The outcome type.
     */
    public OutcomeType getOutcomeType()
    {
        return outcomeType;
    }

    /**
     * Returns the payout type.
     * @return The payout type.
     */
    public PayoutType getPayoutType(){return payoutType;}

    /**
     * Returns the stamina change.
     * @return The stamina change.
     */
    public double getStaminaChange()
    {
        return staminaChange == null ? 0.0 : staminaChange;
    }

    /**
     * Returns whether explicit stamina change.
     * @return True if explicit stamina change, otherwise false.
     */
    public boolean hasExplicitStaminaChange()
    {
        return staminaChange != null;
    }

    /**
     * Returns the crowd energy change.
     * @return The crowd energy change.
     */
    public int getCrowdEnergyChange()
    {
        return crowdEnergyChange;
    }

    /**
     * Returns the concert ends.
     * @return The concert ends.
     */
    public boolean getConcertEnds()
    {
        return concertEnds;
    }
}
