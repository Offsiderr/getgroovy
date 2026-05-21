package seng201.team67.models.enums.questions;

/**
 * Represents the available payout tier values used by the game. Works with the outcome type to provide a reward or punishment
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum PayoutTier {
    /** The easy. */
    EASY("EASY", 300, 100,  -50, -100),
    /** The medium. */
    A_CHALLENGE("MEDIUM", 200, 50,  -75, -200),
    /** The hard. */
    HEARTLESS("HARD", 150, 25,  -100, -300);
    
    /** Text value for the difficulty. */
    private final String difficulty;
    /** Numeric value for the major reward. */
    private final double majorReward;
    /** Numeric value for the minor reward. */
    private final double minorReward;
    /** Numeric value for the minor penalty. */
    private final double minorPenalty;
    /** Numeric value for the major penalty. */
    private final double majorPenalty;


    /**
     * Creates a new payout tier.
     * @param difficulty the text value for the difficulty
     * @param majorReward the numeric value for the major reward
     * @param minorReward the numeric value for the minor reward
     * @param minorPenalty the numeric value for the minor penalty
     * @param majorPenalty the numeric value for the major penalty
     */
    PayoutTier(String difficulty, double majorReward, double minorReward, double minorPenalty, double majorPenalty)
    {
        this.difficulty = difficulty;
        this.majorReward = majorReward;
        this.minorReward = minorReward;
        this.minorPenalty = minorPenalty;
        this.majorPenalty = majorPenalty;
    }

    /**
     * Processes the resolve.
     * It updates related state as needed while performing the operation.
     * @param type the type
     * @return The resolve.
     */
    public double resolve(PayoutType type) {
        return switch (type) {
            case MAJOR_REWARD -> majorReward;
            case MINOR_REWARD -> minorReward;
            case MINOR_PENALTY -> minorPenalty;
            case MAJOR_PENALTY -> majorPenalty;
            case NONE -> 0.0;
        };
    }
}
