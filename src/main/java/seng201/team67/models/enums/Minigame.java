package seng201.team67.models.enums;

import seng201.team67.models.minigames.MiniGameResult;

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



    private final String soundEngineerPath = "/fxml/minigames/SoundMixer.fxml";
    private final String micTimingPath = "/fxml/minigames/MicTiming.fxml";
    private final String crowdHypePath = "/fxml/minigames/CrowdHype.fxml";
    private final String crowdWavePath = "/fxml/minigames/CrowdWave.fxml";


    private final int minCrowdmeterPercentage;
    private final int maxCrowdmeterPercentage;
    private final double maxReward;
    private final MiniGameResult perfectResult;
    private final MiniGameResult goodResult;
    private final MiniGameResult badResult;

    Minigame(int minCrowdmeterPercentage, int maxCrowdmeterPercentage, double maxReward, MiniGameResult perfectResult, MiniGameResult goodResult, MiniGameResult badResult )
    {
        this.minCrowdmeterPercentage = minCrowdmeterPercentage;
        this.maxCrowdmeterPercentage = maxCrowdmeterPercentage;
        this.maxReward = maxReward;
        this.perfectResult = perfectResult;
        this.goodResult = goodResult;
        this.badResult = badResult;
    }

    public boolean isEligible(int crowdMeter)
    {
        Boolean returnValue = false;
        if(crowdMeter > minCrowdmeterPercentage & crowdMeter < maxCrowdmeterPercentage){returnValue = true;}
        return returnValue;
    }

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

    public MiniGameResult getPerfectResult()
    {
        return perfectResult;
    }

    public MiniGameResult getGoodResult()
    {
        return goodResult;
    }

    public MiniGameResult getBadResult()
    {
        return badResult;
    }

}