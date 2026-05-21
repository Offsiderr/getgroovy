package seng201.team67.models.enums;

import seng201.team67.models.minigames.MiniGameResult;

/**
 * Represents the available minigames used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum Minigame {

    SOUNDENGINEER(20, 100, 200,
            new MiniGameResult(30, 0),
            new MiniGameResult(10, 0),
            new MiniGameResult(-15, 0)
    ),

    MICTIMING(20, 100, 200,new MiniGameResult(30, 0),
            new MiniGameResult(10, 0),
            new MiniGameResult(-10, 0)
    ),

    CROWDHYPE(20, 100, 200,
            new MiniGameResult(30, 0),
            new MiniGameResult(10, 0),
            new MiniGameResult(-10, 0)
    ),

    CROWDWAVE(20, 100, 200,
            new MiniGameResult(30, 0),
            new MiniGameResult(10, 0),
            new MiniGameResult(-10, 0)
    );



    /** Text value for the sound engineer path. */
    private final String soundEngineerPath = "/fxml/minigames/SoundMixer.fxml";
    /** Text value for the mic timing path. */
    private final String micTimingPath = "/fxml/minigames/MicTiming.fxml";
    /** Text value for the crowd hype path. */
    private final String crowdHypePath = "/fxml/minigames/CrowdHype.fxml";
    /** Text value for the crowd wave path. */
    private final String crowdWavePath = "/fxml/minigames/CrowdWave.fxml";


    /** Numeric value for the min crowdmeter percentage. */
    private final int minCrowdmeterPercentage;
    /** Numeric value for the max crowdmeter percentage. */
    private final int maxCrowdmeterPercentage;
    /** Numeric value for the max reward. */
    private final double maxReward;
    /** The perfect result. */
    private final MiniGameResult perfectResult;
    /** The good result. */
    private final MiniGameResult goodResult;
    /** The bad result. */
    private final MiniGameResult badResult;

    /**
     * Creates a new minigame.
     * @param minCrowdmeterPercentage the numeric value for the min crowdmeter percentage
     * @param maxCrowdmeterPercentage the numeric value for the max crowdmeter percentage
     * @param maxReward the numeric value for the max reward
     * @param perfectResult the perfect result
     * @param goodResult the good result
     * @param badResult the bad result
     */
    Minigame(int minCrowdmeterPercentage, int maxCrowdmeterPercentage, double maxReward, MiniGameResult perfectResult, MiniGameResult goodResult, MiniGameResult badResult )
    {
        this.minCrowdmeterPercentage = minCrowdmeterPercentage;
        this.maxCrowdmeterPercentage = maxCrowdmeterPercentage;
        this.maxReward = maxReward;
        this.perfectResult = perfectResult;
        this.goodResult = goodResult;
        this.badResult = badResult;
    }

    /**
     * Returns whether eligible.
     * @param crowdMeter the numeric value for the crowd meter
     * @return True if eligible, otherwise false.
     */
    public boolean isEligible(int crowdMeter)
    {
        Boolean returnValue = false;
        if(crowdMeter > minCrowdmeterPercentage & crowdMeter < maxCrowdmeterPercentage){returnValue = true;}
        return returnValue;
    }

    /**
     * Processes the path.
     * It updates related state as needed while performing the operation.
     * @return The resolved path.
     */
    public String path()
    {
        switch (this)
        {
            case SOUNDENGINEER:
                return soundEngineerPath;
            case MICTIMING:
                return micTimingPath;
            case CROWDHYPE:
                return crowdHypePath;
            case CROWDWAVE:
                return crowdWavePath;
            default:
                return null;
        }
    }

    /**
     * Returns the perfect result.
     * @return The perfect result.
     */
    public MiniGameResult getPerfectResult()
    {
        return perfectResult;
    }

    /**
     * Returns the good result.
     * @return The good result.
     */
    public MiniGameResult getGoodResult()
    {
        return goodResult;
    }

    /**
     * Returns the bad result.
     * @return The bad result.
     */
    public MiniGameResult getBadResult()
    {
        return badResult;
    }

}