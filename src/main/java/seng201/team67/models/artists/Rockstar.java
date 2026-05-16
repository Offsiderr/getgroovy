package seng201.team67.models.artists;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rockstar extends Artist {

    //(Jackson needs this for Json deserialisation)
    public Rockstar()
    {
        super("", 0, 80, 100, "");
    }

    @JsonCreator
    public Rockstar(@JsonProperty("name") String name,
                    @JsonProperty("starPower") int starPower,
                    @JsonProperty("description") String description)
    {
        super(name, starPower, 80, 100, description);
    }

    @Override
    public String getType()
    {
        return "Rockstar";
    }
}
