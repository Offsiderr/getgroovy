package seng201.team67.models;

import seng201.team67.models.enums.TourType;

import java.util.List;

public class Tour {

    public final TourType type;
    //private final List<Concert> concerts;
    public int currentStopIndex;
    public int creditsEarned;

    public Tour(TourType type)
    {
        this.type = type;
       //this.concerts = concerts; I have questions about doing it this way
    }
}
