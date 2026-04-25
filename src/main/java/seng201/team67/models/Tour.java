package seng201.team67.models;

import javafx.scene.control.CheckBox;
import seng201.team67.models.enums.TourType;

import java.util.ArrayList;
import java.util.List;

public class Tour {

    public final TourType type;
    private List<Integer> stopOrder;
    public int currentStopIndex;
    public int creditsEarned;
    public boolean concertFinished = false;

    public Tour(TourType type)
    {
        this.type = type;
       //this.concerts = concerts; I have questions about doing it this way
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
}
