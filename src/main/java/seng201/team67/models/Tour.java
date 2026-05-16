package seng201.team67.models;

import seng201.team67.models.enums.TourType;

import java.util.ArrayList;
import java.util.List;

public class Tour {

    public final TourType type;
    private List<Integer> stopOrder;
    private List<ConcertResults> concertResults;
    public int currentStopIndex;
    public int currentLineupStaminaIndex;
    public double creditsEarned;
    public double accruedArtistPay;
    public double totalStaminaDrained;
    public boolean concertFinished = false;
    public boolean endedByExhaustion = false;
    public boolean artistPaySettled = false;
    public double exhaustionRefund = 0;
    public String conditionalEffectText = "";

    public Tour(TourType type)
    {
        this.type = type;
        this.concertResults = new ArrayList<>();
    }

    public void addStamina(double stamina)
    {
        totalStaminaDrained += stamina;
    }

    public double getTotalStaminaDrained()
    {
        return totalStaminaDrained;
    }

    public void setStopOrder(List<Integer> stopOrder)
    {
        this.stopOrder = stopOrder;
    }

    public List<Integer> getStopOrder()
    {
        return stopOrder;
    }

    public boolean hasStopOrder()
    {
        return stopOrder != null;
    }

    public boolean isComplete() {
        return type.getStops() > 0 && currentStopIndex >= type.getStops();
    }

    public double getCreditsEarned()
    {
        return creditsEarned;
    }

    public void addCreditsEarned(double creditsEarned)
    {
        this.creditsEarned += creditsEarned;
    }

    public double getAccruedArtistPay()
    {
        return accruedArtistPay;
    }

    public void addAccruedArtistPay(double accruedArtistPay)
    {
        this.accruedArtistPay += accruedArtistPay;
    }

    public double getPayMultiplier()
    {
        return type.getPayMultiplier();
    }

    public void addConcertResult(ConcertResults concertResult)
    {
        concertResults.add(concertResult);
    }

    public List<ConcertResults> getConcertResults()
    {
        return concertResults;
    }

    public void setEndedByExhaustion(boolean endedByExhaustion)
    {
        this.endedByExhaustion = endedByExhaustion;
    }

    public boolean isEndedByExhaustion()
    {
        return endedByExhaustion;
    }

    public boolean isArtistPaySettled()
    {
        return artistPaySettled;
    }

    public void setArtistPaySettled(boolean artistPaySettled)
    {
        this.artistPaySettled = artistPaySettled;
    }

    public void setExhaustionRefund(double exhaustionRefund)
    {
        this.exhaustionRefund = exhaustionRefund;
    }

    public double getExhaustionRefund()
    {
        return exhaustionRefund;
    }

    public String getConditionalEffectText()
    {
        return conditionalEffectText;
    }

    public void setConditionalEffectText(String conditionalEffectText)
    {
        this.conditionalEffectText = conditionalEffectText;
    }
}
