package seng201.team67.models;

import seng201.team67.GameEnvironment;
import seng201.team67.models.questionmodels.Question;

import java.util.List;

public class Concert {
    private static final int MIN_ENERGY = 0;
    private static final int MAX_ENERGY = 100;

    private List<Question> questions;
    private double maximumRewards;
    private int energy;
    private double ticketSales;

    public Concert(GameEnvironment gameEnvironment)
    {
        ticketSales = gameEnvironment.getConfig().artistRerollCost;
    }

    public int getEnergy()
    {
        return energy;
    }

    public void addEnergy(int toAdd)
    {
        setEnergy(energy + toAdd);
    }

    public void setEnergy(int energy)
    {
        this.energy = Math.max(MIN_ENERGY, Math.min(MAX_ENERGY, energy));
    }

    public double getTicketSales()
    {
        return ticketSales;
    }
}
