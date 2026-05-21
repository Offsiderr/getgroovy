package seng201.team67.models.minigames;

/**
 * Represents the mini game result used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MiniGameResult {

    /** Numeric value for the crowd meter result. This is how much the crowd meter goes up by. */
    private final int crowdMeterResult;
    /** Numeric value for the credit result. */
    private final int creditResult;

    /**
     * Creates a new mini game result.
     * @param crowdMeterResult the numeric value for the crowd meter result
     * @param creditResult the numeric value for the credit result
     */
    public MiniGameResult(int crowdMeterResult, int creditResult)
    {
        this.crowdMeterResult = crowdMeterResult;
        this.creditResult = creditResult;
    }

    /**
     * Returns the crowd meter result.
     * @return The crowd meter result.
     */
    public int getCrowdMeterResult()
    {
        return crowdMeterResult;
    }

    /**
     * Returns the credit result.
     * @return The credit result.
     */
    public int getCreditResult()
    {
        return creditResult;
    }
}
