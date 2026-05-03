package seng201.team67.services;

import seng201.team67.GameEnvironment;
import seng201.team67.models.Artist;
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

    public void addCreditsEarned(Double earned)
    {
        tour.addCreditsEarned(earned);
    }

    public void tourEnded()
    {
        gameEnvironment.getLabelService().giveMoney(tour.getCreditsEarned());

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
}
