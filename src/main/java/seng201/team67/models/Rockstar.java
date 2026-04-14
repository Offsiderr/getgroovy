package seng201.team67.models;

public class Rockstar extends Artist{

    //(Jackson needs this for Json deserialisation)
    public Rockstar()
    {
        super("", 0, 0, 0, "");
    }

    public Rockstar(String name, int star_power, String description)
    {
        super(name, star_power, 80, 120, description);
    }
}
