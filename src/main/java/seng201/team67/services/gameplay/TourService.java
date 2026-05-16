package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.Minigame;
import seng201.team67.models.enums.TourType;

import java.util.*;
import java.util.stream.Collectors;

public class TourService {

    private Tour tour;
    private GameEnvironment gameEnvironment;
    private static double miniGameTriggerChance; //TODO: decide a percentage
    private final Random random = new Random();

    public TourService(Tour tour, GameEnvironment gameEnvironment)
    {
        this.tour = tour;
        this.gameEnvironment = gameEnvironment;
        this.miniGameTriggerChance = gameEnvironment.getConfig().miniGameTriggerChance;
    }

    public TourType getTourType()
    {
        return tour.type;
    }

    public int getStopIndex()
    {
        return tour.currentStopIndex;
    }

    public boolean getConcertStatus()
    {
        return tour.concertFinished;
    }

    public void setConcertFinished()
    {
        tour.concertFinished = true;
    }

    public void resetConcertFinished()
    {
        tour.concertFinished = false;
    }

    public void increaseStopIndex()
    {
        tour.currentStopIndex += 1;
    }

    public List<Integer> getStopOrder()
    {
        return tour.getStopOrder();
    }

    public void setStopOrder(List<Integer> stopOrder)
    {
        tour.setStopOrder(stopOrder);
    }

    public Boolean hasStopOrder()
    {
        return tour.hasStopOrder();
    }

    public boolean isTourComplete() {
        return tour.isComplete();
    }

    public double getCreditsEarned()
    {
        return tour.getCreditsEarned();
    }

    public double getTourPayMultiplier()
    {
        return tour.getPayMultiplier();
    }

    public double getTourArtistPay()
    {
        return gameEnvironment.getLabelService().getLineupTotalPay(tour.type);
    }

    public double getTourArtistPayMultiplier()
    {
        return gameEnvironment.getConfig().getArtistPayMultiplier(tour.type);
    }

    public void addCreditsEarned(Double earned)
    {
        tour.addCreditsEarned(earned);
    }

    public double getAccruedArtistPay()
    {
        return tour.getAccruedArtistPay();
    }

    public void addAccruedArtistPay(double accruedArtistPay)
    {
        tour.addAccruedArtistPay(accruedArtistPay);
    }

    public void addConcertResult(ConcertResults concertResult)
    {
        tour.addConcertResult(concertResult);
    }

    public List<ConcertResults> getConcertResults()
    {
        return tour.getConcertResults();
    }

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

    public void addStamina(double stamina)
    {
        tour.addStamina(stamina);
    }

    public int getCurrentLineupStaminaIndex()
    {
        return tour.currentLineupStaminaIndex;
    }

    public void advanceLineupStaminaIndex()
    {
        tour.currentLineupStaminaIndex += 1;
    }

    public Double getTotalStamina()
    {
        return tour.getTotalStaminaDrained();
    }

    public boolean isLineupExhausted()
    {
        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        return !lineup.isEmpty() && lineup.stream().allMatch(artist -> artist.getStamina() <= 0);
    }

    public int getRemainingConcerts()
    {
        return Math.max(0, tour.type.getStops() - tour.currentStopIndex);
    }

    public void endTourDueToExhaustion()
    {
        int remainingConcerts = getRemainingConcerts();
        double refund = remainingConcerts * gameEnvironment.getConfig().cancelTourPenalty;
        tour.setEndedByExhaustion(true);
        tour.setExhaustionRefund(refund);
        if (refund > 0)
        {
            tour.addCreditsEarned(-refund);
        }
    }

    public boolean isEndedByExhaustion()
    {
        return tour.isEndedByExhaustion();
    }

    public double getExhaustionRefund()
    {
        return tour.getExhaustionRefund();
    }

    public String getConditionalEffectText()
    {
        return tour.getConditionalEffectText();
    }

    public void setConditionalEffectText(String conditionalEffectText)
    {
        tour.setConditionalEffectText(conditionalEffectText);
    }
}
