package seng201.team67.models.enums.questions;

public enum PayoutTier {
    EASY("EASY", 300, 100,  -50, -100),
    MEDIUM("MEDIUM", 200, 50,  -75, -200),
    HARD("HARD", 150, 25,  -100, -300);
    
    private final String difficulty;
    private final double majorReward;
    private final double minorReward;
    private final double minorPenalty;
    private final double majorPenalty;


    PayoutTier(String difficulty, double majorReward, double minorReward, double minorPenalty, double majorPenalty)
    {
        this.difficulty = difficulty;
        this.majorReward = majorReward;
        this.minorReward = minorReward;
        this.minorPenalty = minorPenalty;
        this.majorPenalty = majorPenalty;
    }

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
