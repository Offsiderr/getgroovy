package seng201.team67.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PayoutTier {
    EASY("EASY", 300, 100,  -50, -100),
    MEDIUM("MEDIUM", 200, 50,  -75, -200),
    HARD("HARD", 150, 25,  -100, -300);

    // fields: greatPayout, okPayout, nonePayout, badPayout, terriblePayout

    private final String difficulty;
    private final double greatPayout;
    private final double okPayout;
    private final double badPenalty;
    private final double terriblePenalty;


    PayoutTier(String difficulty, double greatPayout, double okPayout, double badPenalty, double terriblePenalty)
    {
        this.difficulty = difficulty;
        this.greatPayout = greatPayout;
        this.okPayout = okPayout;
        this.badPenalty = badPenalty;
        this.terriblePenalty = terriblePenalty;
    }

    public double resolve(PayoutType type) {
        return switch (type) {
            case GREAT_PAYOUT     -> greatPayout;
            case OK_PAYOUT        -> okPayout;
            case BAD_PENALTY      -> badPenalty;
            case TERRIBLE_PENALTY -> terriblePenalty;
            case NONE             -> 0.0;
        };
    }
}
