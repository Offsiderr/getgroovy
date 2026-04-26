package seng201.team67.models.enums;

import seng201.team67.gui.controllers.instantiable.SoundEngineerStandoffController;
import seng201.team67.models.MiniGameResult;
import seng201.team67.models.minigames.SoundEngineerStandoff;

public enum Minigame {

    SOUNDENGINEER(20, 100, 200,
            new MiniGameResult(30, 0),
            new MiniGameResult(10, 0),
            new MiniGameResult(-15, 0)
            );


    //Paths
    private final String soundEngineerPath = "/fxml/soundMixer.fxml";


    private final int minCrowdmeterPercentage;
    private final int maxCrowdmeterPercentage;
    private final double maxReward;
    private final MiniGameResult perfectResult;
    private final MiniGameResult goodResult;
    private final MiniGameResult badResult;

    Minigame(int minCrowdmeterPercentage, int maxCrowdmeterPercentage, double maxReward, MiniGameResult perfectResult, MiniGameResult goodResult, MiniGameResult badResult)
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
