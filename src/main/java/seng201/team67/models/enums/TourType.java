package seng201.team67.models.enums;

public enum TourType {

    LOCAL(3, 1.0, 0),
    COUNTRY(6, 1.25, 2),
    WORLD(9, 1.75, 4);

    //TODO: add the expedititions unlocked filter thing

    private final int stops;
    private final double payMultiplier;
    private final int expeditionsUnlocked; //How many expeditions before we can do this kind of tour.


    TourType(int stops, double payMultiplier, int expeditionsUnlocked)
    {
        this.stops = stops;
        this.payMultiplier = payMultiplier;
        this.expeditionsUnlocked = expeditionsUnlocked;
    }

    public int getStops(){return stops;}
    public double getPayMultiplier(){return payMultiplier;}
    public int getExpeditionsUnlocked(){return expeditionsUnlocked;}


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
