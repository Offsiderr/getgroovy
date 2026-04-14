package seng201.team67.models;

public class Rapper extends Artist{

    //(Jackson needs this for Json deserialisation)
    public Rapper()
    {
        super("", 0, 0, 0, "");
    }

    public Rapper (String name, int star_power, String description)
    {
        super(name, star_power, 120, 80, description);

    }
}
