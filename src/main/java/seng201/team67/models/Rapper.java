package seng201.team67.models;

public class Rapper extends Artist{

    //(Jackson needs this for Json deserialisation)
    public Rapper()
    {
        super("", 0, 120, 100, "");
    }

    public Rapper (String name, int starPower, String description)
    {
        super(name, starPower, 120, 100, description);
    }

    @Override
    public String getType()
    {
        return "Rapper";
    }
}
