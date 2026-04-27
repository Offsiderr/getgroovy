package seng201.team67.models.enums;

public enum TourType {

    LOCAL(3, 1.0, 0, 5),
    COUNTRY(6, 2.5, 2, 6),
    WORLD(9, 3, 4, 8);

    //TODO: add the expedititions unlocked filter thing

    private final int stops;
    private final double payMultiplier; //Ticket sales/pay multiplier
    private final int expeditionsUnlocked; //How many expeditions before we can do this kind of tour.
    private final int baseStaminaDrain;


    TourType(int stops, double payMultiplier, int expeditionsUnlocked, int baseStaminaDrain)
    {
        this.stops = stops;
        this.payMultiplier = payMultiplier;
        this.expeditionsUnlocked = expeditionsUnlocked;
        this.baseStaminaDrain = baseStaminaDrain;
    }

    public int getStops(){return stops;}
    public double getPayMultiplier(){return payMultiplier;}
    public int getExpeditionsUnlocked(){return expeditionsUnlocked;}
    public int getBaseStaminaDrain(){return baseStaminaDrain;}

    //while this is the to string method, you must adjust the concert controller class methods
    //if you adjust this, as they rely on this method
    @Override
    public String toString() {
        return switch (this) {
            case LOCAL   -> "local";
            case COUNTRY -> "country";
            case WORLD   -> "world";
        };
    }
}
