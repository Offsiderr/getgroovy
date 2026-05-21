package seng201.team67.models.enums;

import seng201.team67.models.enums.questions.PayoutTier;

/**
 * Represents the available difficulty values used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum Difficulty {

    /** The easy. */
    EASY("Easy", 500, 1.0, 0.1, PayoutTier.EASY),
    /** The achallenge. */
    A_CHALLENGE("A Challenge", 300, 1.25, 0.2, PayoutTier.A_CHALLENGE),
    /** The heartless. */
    HEARTLESS("Heartless", 150, 1.5, 0.35, PayoutTier.HEARTLESS);

    /** Text value for the display name. */
    private final String displayName;
    /** Numeric value for the starting credits. */
    private final int startingCredits;
    /** Numeric value for the pay multiplier. */
    private final double payMultiplier;
    /** Numeric value for the bad event chance. */
    private final double badEventChance;
    /** The payout tier. */
    private final PayoutTier payoutTier;

    /**
     * Creates a new difficulty.
     * @param displayName the text value for the display name
     * @param startingCredits the numeric value for the starting credits
     * @param payMultiplier the numeric value for the pay multiplier
     * @param badEventChance the numeric value for the bad event chance
     * @param payoutTier the payout tier
     */
    Difficulty(String displayName, int startingCredits, double payMultiplier, double badEventChance, PayoutTier payoutTier) {
        this.displayName = displayName;
        this.startingCredits = startingCredits;
        this.payMultiplier = payMultiplier;
        this.badEventChance = badEventChance;
        this.payoutTier = payoutTier;
    }

    //Switch the other geters and setters to this style eventually, far more efficent in terms of space :)
    /**
     * Returns the display name.
     * @return The display name.
     */
    public String getDisplayName() { return displayName; }
    /**
     * Returns the starting credits.
     * @return The starting credits.
     */
    public int getStartingCredits() { return startingCredits; }
    /**
     * Returns the pay multiplier.
     * @return The pay multiplier.
     */
    public double getPayMultiplier() { return payMultiplier; }
    /**
     * Returns the bad event chance.
     * @return The bad event chance.
     */
    public double getBadEventChance() { return badEventChance; }
}
