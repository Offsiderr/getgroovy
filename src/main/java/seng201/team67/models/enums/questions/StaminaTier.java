package seng201.team67.models.enums.questions;

/**
 * Represents the available stamina tier values used by the outcomes.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum StaminaTier {
    /** The easy stamina tier. */
    EASY(0, -5, -10, 0, -15, -20),
    /** The medium. stamina tier*/
    A_CHALLENGE(-5, -10, -15, 0, -20, -25),
    /** The hard. stamina tier*/
    HEARTLESS(-10, -15, -20, 0, -25, -30);

    /** Numeric value for the great change. */
    private final double greatChange;
    /** Numeric value for the good change. */
    private final double goodChange;
    /** Numeric value for the ok change. */
    private final double okChange;
    /** Numeric value for the none change. */
    private final double noneChange;
    /** Numeric value for the bad change. */
    private final double badChange;
    /** Numeric value for the terrible change. */
    private final double terribleChange;

    /**
     * Creates a new stamina tier.
     * @param greatChange the numeric value for the great change
     * @param goodChange the numeric value for the good change
     * @param okChange the numeric value for the ok change
     * @param noneChange the numeric value for the none change
     * @param badChange the numeric value for the bad change
     * @param terribleChange the numeric value for the terrible change
     */
    StaminaTier(double greatChange, double goodChange, double okChange,
                double noneChange, double badChange, double terribleChange)
    {
        this.greatChange = greatChange;
        this.goodChange = goodChange;
        this.okChange = okChange;
        this.noneChange = noneChange;
        this.badChange = badChange;
        this.terribleChange = terribleChange;
    }

    /**
     * Processes the resolve.
     * It updates related state as needed while performing the operation.
     * @param type the type
     * @return The resolve.
     */
    public double resolve(OutcomeType type)
    {
        return switch (type) {
            case GREAT -> greatChange;
            case GOOD -> goodChange;
            case OK -> okChange;
            case NONE -> noneChange;
            case BAD -> badChange;
            case TERRIBLE -> terribleChange;
        };
    }
}
