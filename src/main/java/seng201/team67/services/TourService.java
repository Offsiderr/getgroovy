package seng201.team67.services;

import seng201.team67.models.Tour;
import seng201.team67.models.enums.TourType;

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
}
