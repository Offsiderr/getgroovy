package seng201.team67.models.artists;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the rapper used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class Rapper extends Artist {

    //(Jackson needs this for Json deserialisation)
    /**
     * Creates a new rapper.
     */
    public Rapper()
    {
        super("", 0, 120, 100, "");
    }

    /**
     * Creates a new rapper.
     * @param name the name value to use
     * @param starPower the star power value to apply
     * @param description the description text to use
     */
    @JsonCreator
    public Rapper(@JsonProperty("name") String name,
                  @JsonProperty("starPower") int starPower,
                  @JsonProperty("description") String description)
    {
        super(name, starPower, 120, 100, description);
    }

    /**
     * Returns the type.
     * @return The type.
     */
    @Override
    public String getType()
    {
        return "Rapper";
    }
}
