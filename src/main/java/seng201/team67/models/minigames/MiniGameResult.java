package seng201.team67.models.minigames;

public class MiniGameResult {

    private final int crowdMeterResult; //GOES up by, not gets set to
    private final int creditResult;

    public MiniGameResult(int crowdMeterResult, int creditResult)
    {
        this.crowdMeterResult = crowdMeterResult;
        this.creditResult = creditResult;
    }

    public int getCrowdMeterResult()
    {
        return crowdMeterResult;
    }

    public int getCreditResult()
    {
        return creditResult;
    }
}
