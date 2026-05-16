package seng201.team67.models.artists;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rapper extends Artist {

    //(Jackson needs this for Json deserialisation)
    public Rapper()
    {
        super("", 0, 120, 100, "");
    }

    @JsonCreator
    public Rapper(@JsonProperty("name") String name,
                  @JsonProperty("starPower") int starPower,
                  @JsonProperty("description") String description)
    {
        super(name, starPower, 120, 100, description);
    }

    @Override
    public String getType()
    {
        return "Rapper";
    }
}
