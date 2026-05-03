package seng201.team67.models;

import seng201.team67.models.enums.TourType;

import java.util.List;

public class Tour {

    public final TourType type;
    private List<Integer> stopOrder;
    public int currentStopIndex;
    public int currentLineupStaminaIndex;
    public double creditsEarned;
    public double totalStaminaDrained;
    public boolean concertFinished = false;
    public boolean endedByExhaustion = false;
    public double exhaustionRefund = 0;

    public Tour(TourType type)
    {
        this.type = type;
       //this.concerts = concerts; I have questions about doing it this way
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

    public double getPayMultiplier()
    {
        return type.getPayMultiplier();
    }

    public void setEndedByExhaustion(boolean endedByExhaustion)
    {
        this.endedByExhaustion = endedByExhaustion;
    }

    public boolean isEndedByExhaustion()
    {
        return endedByExhaustion;
    }

    public void setExhaustionRefund(double exhaustionRefund)
    {
        this.exhaustionRefund = exhaustionRefund;
    }

    public double getExhaustionRefund()
    {
        return exhaustionRefund;
    }
}
