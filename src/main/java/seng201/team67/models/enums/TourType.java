package seng201.team67.models.enums;

/**
 * Represents the available tour types in the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum TourType {

    /** The local. */
    LOCAL(3, 1.0, 0, 5),
    /** The country. */
    COUNTRY(6, 2.5, 2, 6),
    /** The world. */
    WORLD(9, 3, 4, 8);

    /** Numeric value for the stops. */
    private final int stops;
    /** Numeric value for the pay multiplier. */
    private final double payMultiplier; //Ticket sales/pay multiplier
    /** Numeric value for the expeditions unlocked. */
    private final int expeditionsUnlocked; //How many expeditions before we can do this kind of tour.
    /** Numeric value for the base stamina drain. */
    private final int baseStaminaDrain;


    /**
     * Creates a new tour type.
     * @param stops the numeric value for the stops
     * @param payMultiplier the numeric value for the pay multiplier
     * @param expeditionsUnlocked the numeric value for the expeditions unlocked
     * @param baseStaminaDrain the numeric value for the base stamina drain
     */
    TourType(int stops, double payMultiplier, int expeditionsUnlocked, int baseStaminaDrain)
    {
        this.stops = stops;
        this.payMultiplier = payMultiplier;
        this.expeditionsUnlocked = expeditionsUnlocked;
        this.baseStaminaDrain = baseStaminaDrain;
    }

    /**
     * Returns the stops.
     * @return The stops.
     */
    public int getStops(){return stops;}
    /**
     * Returns the pay multiplier.
     * @return The pay multiplier.
     */
    public double getPayMultiplier(){return payMultiplier;}
    /**
     * Returns the expeditions unlocked.
     * @return The expeditions unlocked.
     */
    public int getExpeditionsUnlocked(){return expeditionsUnlocked;}
    /**
     * Returns the base stamina drain.
     * @return The base stamina drain.
     */
    public int getBaseStaminaDrain(){return baseStaminaDrain;}

    @Override
    public String toString() {
        return switch (this) {
            case LOCAL   -> "local";
            case COUNTRY -> "country";
            case WORLD   -> "world";
        };
    }
}
