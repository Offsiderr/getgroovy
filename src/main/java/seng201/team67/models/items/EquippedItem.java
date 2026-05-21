package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.ItemEffects;
import seng201.team67.models.enums.Rarity;

import java.util.List;

/**
 * The equipped item type. This means that it can be attached to an artist and can be used an unlimited amount of times.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class EquippedItem extends Item{

    /**
     * Creates a new equipped item.
     */
    public EquippedItem() {
        super();
    }

    /**
     * Creates a new equipped item.
     * @param name the name value to use
     * @param description the description text to use
     * @param cost the numeric value for the cost
     * @param rarity the rarity
     * @param itemEffects the list of item effects
     */
    @JsonCreator
    public EquippedItem(@JsonProperty("name") String name,
                        @JsonProperty("description") String description,
                        @JsonProperty("cost") int cost,
                        @JsonProperty("rarity") Rarity rarity,
                        @JsonProperty("effects") @JsonAlias("effect") List<ItemEffects> itemEffects) {
        super(name, description, cost, rarity, itemEffects);
    }

    /**
     * Returns the type.
     * @return The type.
     */
    public String getType()
    {
        return "Equipable";
    }

}
