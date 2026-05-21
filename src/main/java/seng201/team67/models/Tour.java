package seng201.team67.models;

import seng201.team67.models.enums.TourType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the tour used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class Tour {

    /** The type. */
    public final TourType type;
    /** Collection that stores the stop order. */
    private List<Integer> stopOrder;
    /** Collection that stores the concert results. */
    private List<ConcertResults> concertResults;
    /** Numeric value for the current stop index. */
    public int currentStopIndex;
    /** Numeric value for the current lineup stamina index. */
    public int currentLineupStaminaIndex;
    /** Numeric value for the credits earned. */
    public double creditsEarned;
    /** Numeric value for the accrued artist pay. */
    public double accruedArtistPay;
    /** Numeric value for the total stamina drained. */
    public double totalStaminaDrained;
    /** Whether concert finished. */
    public boolean concertFinished = false;
    /** Whether ended by exhaustion. */
    public boolean endedByExhaustion = false;
    /** Whether artist pay settled. */
    public boolean artistPaySettled = false;
    /** Numeric value for the exhaustion refund. */
    public double exhaustionRefund = 0;
    /** Numeric value for the early cancellation refund. */
    public double cancellationRefund = 0;
    /** Text value for the conditional effect text. */
    public String conditionalEffectText = "";

    /**
     * Creates a new tour.
     * @param type the type
     */
    public Tour(TourType type)
    {
        this.type = type;
        this.concertResults = new ArrayList<>();
    }

    /**
     * Adds the stamina.
     * @param stamina the stamina value to apply
     */
    public void addStamina(double stamina)
    {
        totalStaminaDrained += stamina;
    }

    /**
     * Returns the total stamina drained.
     * @return The total stamina drained.
     */
    public double getTotalStaminaDrained()
    {
        return totalStaminaDrained;
    }

    /**
     * Sets the stop order.
     * @param stopOrder the list of stop order
     */
    public void setStopOrder(List<Integer> stopOrder)
    {
        this.stopOrder = stopOrder;
    }

    /**
     * Returns the stop order.
     * @return The stop order.
     */
    public List<Integer> getStopOrder()
    {
        return stopOrder;
    }

    /**
     * Returns whether stop order.
     * @return True if stop order, otherwise false.
     */
    public boolean hasStopOrder()
    {
        return stopOrder != null;
    }

    /**
     * Returns whether complete.
     * @return True if complete, otherwise false.
     */
    public boolean isComplete() {
        return type.getStops() > 0 && currentStopIndex >= type.getStops();
    }

    /**
     * Returns the credits earned.
     * @return The credits earned.
     */
    public double getCreditsEarned()
    {
        return creditsEarned;
    }

    /**
     * Adds the credits earned.
     * @param creditsEarned the numeric value for the credits earned
     */
    public void addCreditsEarned(double creditsEarned)
    {
        this.creditsEarned += creditsEarned;
    }

    /**
     * Returns the accrued artist pay.
     * @return The accrued artist pay.
     */
    public double getAccruedArtistPay()
    {
        return accruedArtistPay;
    }

    /**
     * Adds the accrued artist pay.
     * @param accruedArtistPay the numeric value for the accrued artist pay
     */
    public void addAccruedArtistPay(double accruedArtistPay)
    {
        this.accruedArtistPay += accruedArtistPay;
    }

    /**
     * Returns the pay multiplier.
     * @return The pay multiplier.
     */
    public double getPayMultiplier()
    {
        return type.getPayMultiplier();
    }

    /**
     * Adds the concert result.
     * @param concertResult the concert result
     */
    public void addConcertResult(ConcertResults concertResult)
    {
        concertResults.add(concertResult);
    }

    /**
     * Returns the concert results.
     * @return The concert results.
     */
    public List<ConcertResults> getConcertResults()
    {
        return concertResults;
    }

    /**
     * Sets the ended by exhaustion.
     * @param endedByExhaustion whether ended by exhaustion
     */
    public void setEndedByExhaustion(boolean endedByExhaustion)
    {
        this.endedByExhaustion = endedByExhaustion;
    }

    /**
     * Returns whether ended by exhaustion.
     * @return True if ended by exhaustion, otherwise false.
     */
    public boolean isEndedByExhaustion()
    {
        return endedByExhaustion;
    }

    /**
     * Returns whether artist pay settled.
     * @return True if artist pay settled, otherwise false.
     */
    public boolean isArtistPaySettled()
    {
        return artistPaySettled;
    }

    /**
     * Sets the artist pay settled.
     * @param artistPaySettled whether artist pay settled
     */
    public void setArtistPaySettled(boolean artistPaySettled)
    {
        this.artistPaySettled = artistPaySettled;
    }

    /**
     * Sets the exhaustion refund.
     * @param exhaustionRefund the numeric value for the exhaustion refund
     */
    public void setExhaustionRefund(double exhaustionRefund)
    {
        this.exhaustionRefund = exhaustionRefund;
    }

    /**
     * Returns the exhaustion refund.
     * @return The exhaustion refund.
     */
    public double getExhaustionRefund()
    {
        return exhaustionRefund;
    }

    /**
     * Sets the cancellation refund.
     * @param cancellationRefund the numeric value for the cancellation refund
     */
    public void setCancellationRefund(double cancellationRefund)
    {
        this.cancellationRefund = cancellationRefund;
    }

    /**
     * Returns the cancellation refund.
     * @return The cancellation refund.
     */
    public double getCancellationRefund()
    {
        return cancellationRefund;
    }

    /**
     * Returns the conditional effect text.
     * @return The conditional effect text.
     */
    public String getConditionalEffectText()
    {
        return conditionalEffectText;
    }

    /**
     * Sets the conditional effect text.
     * @param conditionalEffectText the text value for the conditional effect text
     */
    public void setConditionalEffectText(String conditionalEffectText)
    {
        this.conditionalEffectText = conditionalEffectText;
    }
}
