package seng201.team67.models.enums.questions;

public enum StaminaTier {
    EASY(0, -5, -10, 0, -15, -20),
    MEDIUM(-5, -10, -15, 0, -20, -25),
    HARD(-10, -15, -20, 0, -25, -30);

    private final double greatChange;
    private final double goodChange;
    private final double okChange;
    private final double noneChange;
    private final double badChange;
    private final double terribleChange;

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
