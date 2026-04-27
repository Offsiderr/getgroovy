package seng201.team67.models.enums;

public enum Difficulty {

    EASY("Easy", 500, 1.0, 0.1, PayoutTier.EASY),
    A_CHALLENGE("A Challenge", 300, 1.25, 0.2, PayoutTier.MEDIUM),
    HEARTLESS("Heartless", 150, 1.5, 0.35, PayoutTier.HARD);

    private final String displayName;
    private final int startingCredits;
    private final double payMultiplier;
    private final double badEventChance;
    private final PayoutTier payoutTier;

    Difficulty(String displayName, int startingCredits, double payMultiplier, double badEventChance, PayoutTier payoutTier) {
        this.displayName = displayName;
        this.startingCredits = startingCredits;
        this.payMultiplier = payMultiplier;
        this.badEventChance = badEventChance;
        this.payoutTier = payoutTier;
    }

    //Switch the other geters and setters to this style eventually, far more efficent in terms of space :)
    public String getDisplayName() { return displayName; }
    public int getStartingCredits() { return startingCredits; }
    public double getPayMultiplier() { return payMultiplier; }
    public double getBadEventChance() { return badEventChance; }
}
