package seng201.team67.services;

import javafx.scene.control.CheckBox;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.TourType;

import java.util.ArrayList;
import java.util.List;

public class TourService {

    private Tour tour;

    public TourService(Tour tour)
    {
        this.tour = tour;
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
}
