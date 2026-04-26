package seng201.team67.services;

import seng201.team67.GameEnviroment;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.Minigame;
import seng201.team67.models.enums.TourType;

import java.util.*;
import java.util.stream.Collectors;

public class TourService {

    private Tour tour;
    private GameEnviroment gameEnviroment;
    private static double miniGameTriggerChance; //TODO: decide a percentage
    private final Random random = new Random();

    public TourService(Tour tour, GameEnviroment gameEnviroment)
    {
        this.tour = tour;
        this.gameEnviroment = gameEnviroment;
        this.miniGameTriggerChance = gameEnviroment.getConfig().miniGameTriggerChance;
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

    public void addCreditsEarned(Double earned)
    {
        tour.addCreditsEarned(earned);
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
}
