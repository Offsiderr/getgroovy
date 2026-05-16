package seng201.team67.models.artists;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Popstar extends Artist {

    //(Jackson needs this for Json deserialisation)
    public Popstar()
    {
        super("", 0, 100, 100,  "");
    }

    @JsonCreator
    public Popstar(@JsonProperty("name") String name,
                   @JsonProperty("starPower") int starPower,
                   @JsonProperty("description") String description)
    {
        super(name, starPower, 100, 100, description);
    }

    @Override
    public String getType()
    {
        return "Popstar";
    }
}
