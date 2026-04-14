package seng201.team67.models.enums;

public enum Difficulty {

    EASY("Easy", 500, 1.0, 0.1),
    A_CHALLENGE("A Challenge", 300, 1.25, 0.2),
    HEARTLESS("Heartless", 150, 1.5, 0.35);

    private final String displayName;
    private final int startingCredits;
    private final double payMultiplier;
    private final double badEventChance;

    Difficulty(String displayName, int startingCredits, double payMultiplier, double badEventChance) {
        this.displayName = displayName;
        this.startingCredits = startingCredits;
        this.payMultiplier = payMultiplier;
        this.badEventChance = badEventChance;
    }

    //Switch the other geters and setters to this style eventually, far more efficent in terms of space :)
    public String getDisplayName() { return displayName; }
    public int getStartingCredits() { return startingCredits; }
    public double getPayMultiplier() { return payMultiplier; }
    public double getBadEventChance() { return badEventChance; }
}
