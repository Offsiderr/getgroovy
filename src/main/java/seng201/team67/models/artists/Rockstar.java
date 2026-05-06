package seng201.team67.models.artists;

public class Rockstar extends Artist {

    //(Jackson needs this for Json deserialisation)
    public Rockstar()
    {
        super("", 0, 80, 100, "");
    }

    public Rockstar(String name, int starPower, String description)
    {
        super(name, starPower, 80, 100, description);
    }

    @Override
    public String getType()
    {
        return "Rockstar";
    }
}
