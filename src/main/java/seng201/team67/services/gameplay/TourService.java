package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.Minigame;
import seng201.team67.models.enums.TourType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides tour operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class TourService {

    /** The tour. */
    private Tour tour;
    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;
    /** Numeric value for the mini game trigger chance. */
    private static double miniGameTriggerChance;
    /** The random. */
    private final Random random = new Random();

    /**
     * Creates a new tour service.
     * @param tour the tour
     * @param gameEnvironment the active game environment
     */
    public TourService(Tour tour, GameEnvironment gameEnvironment)
    {
        this.tour = tour;
        this.gameEnvironment = gameEnvironment;
        this.miniGameTriggerChance = gameEnvironment.getConfig().miniGameTriggerChance;
    }

    /**
     * Returns the tour type.
     * @return The tour type.
     */
    public TourType getTourType()
    {
        return tour.type;
    }

    /**
     * Returns the stop index.
     * @return The stop index.
     */
    public int getStopIndex()
    {
        return tour.currentStopIndex;
    }

    /**
     * Returns the concert status.
     * @return The concert status.
     */
    public boolean getConcertStatus()
    {
        return tour.concertFinished;
    }

    /**
     * Sets the concert finished.
     */
    public void setConcertFinished()
    {
        tour.concertFinished = true;
    }

    /**
     * Resets the concert finished.
     */
    public void resetConcertFinished()
    {
        tour.concertFinished = false;
    }

    /**
     * Increases the stop index.
     */
    public void increaseStopIndex()
    {
        tour.currentStopIndex += 1;
    }

    /**
     * Returns the stop order.
     * @return The stop order.
     */
    public List<Integer> getStopOrder()
    {
        return tour.getStopOrder();
    }

    /**
     * Sets the stop order.
     * @param stopOrder the list of stop order
     */
    public void setStopOrder(List<Integer> stopOrder)
    {
        tour.setStopOrder(stopOrder);
    }

    /**
     * Returns whether stop order.
     * @return True if stop order, otherwise false.
     */
    public Boolean hasStopOrder()
    {
        return tour.hasStopOrder();
    }

    /**
     * Returns whether tour complete.
     * @return True if tour complete, otherwise false.
     */
    public boolean isTourComplete() {
        return tour.isComplete();
    }

    /**
     * Returns the credits earned.
     * @return The credits earned.
     */
    public double getCreditsEarned()
    {
        return tour.getCreditsEarned();
    }

    /**
     * Returns the tour pay multiplier.
     * @return The tour pay multiplier.
     */
    public double getTourPayMultiplier()
    {
        return tour.getPayMultiplier();
    }

    /**
     * Returns the tour artist pay.
     * @return The tour artist pay.
     */
    public double getTourArtistPay()
    {
        return gameEnvironment.getLabelService().getLineupTotalPay(tour.type);
    }

    /**
     * Returns the tour artist pay multiplier.
     * @return The tour artist pay multiplier.
     */
    public double getTourArtistPayMultiplier()
    {
        return gameEnvironment.getConfig().getArtistPayMultiplier(tour.type);
    }

    /**
     * Adds the credits earned.
     * @param earned the numeric value for the earned
     */
    public void addCreditsEarned(Double earned)
    {
        tour.addCreditsEarned(earned);
    }

    /**
     * Returns the accrued artist pay.
     * @return The accrued artist pay.
     */
    public double getAccruedArtistPay()
    {
        return tour.getAccruedArtistPay();
    }

    /**
     * Adds the accrued artist pay.
     * @param accruedArtistPay the numeric value for the accrued artist pay
     */
    public void addAccruedArtistPay(double accruedArtistPay)
    {
        tour.addAccruedArtistPay(accruedArtistPay);
    }

    /**
     * Adds the concert result.
     * @param concertResult the concert result
     */
    public void addConcertResult(ConcertResults concertResult)
    {
        tour.addConcertResult(concertResult);
    }

    /**
     * Returns the concert results.
     * @return The concert results.
     */
    public List<ConcertResults> getConcertResults()
    {
        return tour.getConcertResults();
    }

    /**
     * Processes the tour ended.
     */
    public void tourEnded()
    {
        settleArtistPay();
        updateRetirementChanceForArtistsWithoutABreak();
        gameEnvironment.getLabelService().resetLineupStamina();
    }

    private void updateRetirementChanceForArtistsWithoutABreak()
    {
        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        List<Artist> allArtists = gameEnvironment.getLabelService().getAllArtists();
        int increaseAmount = gameEnvironment.getConfig().retirementChanceIncreasePerThreeToursWithoutBreak;

        for (Artist artist : allArtists)
        {
            if (lineup.contains(artist))
            {
                artist.incrementConsecutiveToursWithoutBreak();
                if (artist.getConsecutiveToursWithoutBreak() % 3 == 0)
                {
                    artist.increaseRetirementChance(increaseAmount);
                }
                continue;
            }

            artist.resetConsecutiveToursWithoutBreak();
        }
    }

    /**
     * Settles the artists pay.
     */
    public void settleArtistPay()
    {
        if (tour.isArtistPaySettled())
        {
            return;
        }

        double accruedArtistPay = tour.getAccruedArtistPay();
        if (accruedArtistPay > 0)
        {
            tour.addCreditsEarned(-accruedArtistPay);
            gameEnvironment.getLabelService().takeMoney(accruedArtistPay);
        }

        tour.setArtistPaySettled(true);
    }

    /**
     * Rolls for a minigame. If the random amount is less than the minigame change, then a random minigame is picked.
     * @param crowdMeter the numeric value for the crowd meter
     * @return The resulting minigame, or `null` if no minigame has been rolled.
     */
    public Minigame rollMiniGameTrigger(int crowdMeter)
    {
        if (random.nextDouble() >= miniGameTriggerChance)
        {
            return null;
        }

        List<Minigame> minigameList = Arrays.stream(Minigame.values()).filter(mg -> mg.isEligible(crowdMeter)).collect(Collectors.toList());

        if (minigameList.isEmpty())
        {
            return null;
        }

        return minigameList.get(random.nextInt(minigameList.size()));
    }

    /**
     * Adds the stamina.
     * @param stamina the stamina value to apply
     */
    public void addStamina(double stamina)
    {
        tour.addStamina(stamina);
    }

    /**
     * Returns the current lineup stamina index.
     * @return The current lineup stamina index.
     */
    public int getCurrentLineupStaminaIndex()
    {
        return tour.currentLineupStaminaIndex;
    }

    /**
     * Processes the advance lineup stamina index.
     */
    public void advanceLineupStaminaIndex()
    {
        tour.currentLineupStaminaIndex += 1;
    }

    /**
     * Returns the total stamina.
     * @return The total stamina.
     */
    public Double getTotalStamina()
    {
        return tour.getTotalStaminaDrained();
    }

    /**
     * Returns whether lineup exhausted.
     * @return True if lineup exhausted, otherwise false.
     */
    public boolean isLineupExhausted()
    {
        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        return !lineup.isEmpty() && lineup.stream().allMatch(artist -> artist.getStamina() <= 0);
    }

    /**
     * Returns the remaining concerts.
     * @return The remaining concerts.
     */
    public int getRemainingConcerts()
    {
        return Math.max(0, tour.type.getStops() - tour.currentStopIndex);
    }

    /**
     * Processes the end tour due to exhaustion.
     */
    public void endTourDueToExhaustion()
    {
        if (tour.isEndedByExhaustion())
        {
            return;
        }

        int remainingConcerts = getRemainingConcerts();
        double refund = remainingConcerts * gameEnvironment.getConfig().getCancelTourPenalty(tour.type);
        tour.setEndedByExhaustion(true);
        tour.setExhaustionRefund(refund);
        applyRefund(refund);
    }

    /**
     * Returns the early cancellation refund for the current tour.
     * @return The refund amount required to cancel the tour early.
     */
    public double getEarlyCancellationCost()
    {
        return gameEnvironment.getConfig().getCancelTourPenalty(tour.type);
    }

    /**
     * Returns whether the label can afford to cancel the current tour early.
     * @return True if the cancellation refund can be paid, otherwise false.
     */
    public boolean canCancelTourEarly()
    {
        return gameEnvironment.getLabelService().getMoney() >= getEarlyCancellationCost();
    }

    /**
     * Processes the end tour early cancellation refund.
     * @return True if the cancellation was applied, otherwise false.
     */
    public boolean cancelTourEarly()
    {
        if (tour.getCancellationRefund() > 0)
        {
            return true;
        }

        if (!canCancelTourEarly())
        {
            return false;
        }

        double refund = getEarlyCancellationCost();
        tour.setCancellationRefund(refund);
        applyRefund(refund);
        return true;
    }

    /**
     * Returns whether ended by exhaustion.
     * @return True if ended by exhaustion, otherwise false.
     */
    public boolean isEndedByExhaustion()
    {
        return tour.isEndedByExhaustion();
    }

    /**
     * Returns the exhaustion refund.
     * @return The exhaustion refund.
     */
    public double getExhaustionRefund()
    {
        return tour.getExhaustionRefund();
    }

    /**
     * Returns the cancellation refund.
     * @return The cancellation refund.
     */
    public double getCancellationRefund()
    {
        return tour.getCancellationRefund();
    }

    /**
     * Returns the conditional effect text.
     * @return The conditional effect text.
     */
    public String getConditionalEffectText()
    {
        return tour.getConditionalEffectText();
    }

    /**
     * Sets the conditional effect text.
     * @param conditionalEffectText the text value for the conditional effect text
     */
    public void setConditionalEffectText(String conditionalEffectText)
    {
        tour.setConditionalEffectText(conditionalEffectText);
    }

    private void applyRefund(double refund)
    {
        if (refund <= 0)
        {
            return;
        }

        tour.addCreditsEarned(-refund);
        gameEnvironment.getLabelService().takeMoney(refund);
    }
}
