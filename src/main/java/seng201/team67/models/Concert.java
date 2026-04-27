package seng201.team67.models;

import seng201.team67.GameEnviroment;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.ConcertService;

import java.util.List;

public class Concert {

    private List<Question> questions;
    private double maximumRewards;
    private int energy;
    private double ticketSales;

    public Concert(GameEnviroment gameEnviroment)
    {
        ticketSales = gameEnviroment.getConfig().artistRerollCost;
    }

    public int getEnergy()
    {
        return energy;
    }

    public void addEnergy(int toAdd)
    {
        energy = energy = toAdd;
    }

    public double getTicketSales()
    {
        return ticketSales;
    }
}
