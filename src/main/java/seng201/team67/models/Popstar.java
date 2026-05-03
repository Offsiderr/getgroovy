package seng201.team67.models;

public class Popstar extends Artist{

    //(Jackson needs this for Json deserialisation)
    public Popstar()
    {
        super("", 0, 100, 100,  "");
    }

    public Popstar(String name, int starPower, String description)
    {
        super(name, starPower, 100, 100, description);
    }

    @Override
    public String getType()
    {
        return "Popstar";
    }
}
