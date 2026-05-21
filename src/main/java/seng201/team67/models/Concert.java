package seng201.team67.models;

import seng201.team67.GameEnvironment;
import seng201.team67.models.questionmodels.Question;

import java.util.List;

/**
 * Represents the concert used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class Concert {
    /** Constant that defines the min energy. */
    private static final int MIN_ENERGY = 0;
    /** Constant that defines the max energy. */
    private static final int MAX_ENERGY = 100;

    /** Collection that stores the questions. */
    private List<Question> questions;
    /** Numeric value for the maximum rewards. */
    private double maximumRewards;
    /** Numeric value for the energy. */
    private int energy;
    /** Numeric value for the ticket sales. */
    private double ticketSales;

    /**
     * Creates a new concert.
     * @param gameEnvironment the active game environment
     */
    public Concert(GameEnvironment gameEnvironment)
    {
        ticketSales = gameEnvironment.getConfig().artistRerollCost;
    }

    /**
     * Returns the energy.
     * @return The energy.
     */
    public int getEnergy()
    {
        return energy;
    }

    /**
     * Adds the energy.
     * @param toAdd the numeric value for the to add
     */
    public void addEnergy(int toAdd)
    {
        setEnergy(energy + toAdd);
    }

    /**
     * Sets the energy.
     * @param energy the numeric value for the energy
     */
    public void setEnergy(int energy)
    {
        this.energy = Math.max(MIN_ENERGY, Math.min(MAX_ENERGY, energy));
    }

    /**
     * Returns the ticket sales.
     * @return The ticket sales.
     */
    public double getTicketSales()
    {
        return ticketSales;
    }
}
