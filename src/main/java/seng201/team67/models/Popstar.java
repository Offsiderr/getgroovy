package seng201.team67.models;

public class Popstar extends Artist{

    //(Jackson needs this for Json deserialisation)
    public Popstar()
    {
        super("", 0, 0, 0, "");
        setStamina(100);
        setBaseStamina(100);
    }

    public Popstar(String name, int star_power, String description)
    {
        super(name, star_power, 100, 100, description);
    }

    @Override
    public String getType()
    {
        return "Popstar";
    }
}
