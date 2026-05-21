package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.ItemEffects;

import java.util.List;

/**
 * The conditional. This means that it is equipped to an artist and is triggered whenever the item's conditions are met.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ConditionalItem extends EquippedItem {

    /**
     * Creates a new conditional item.
     */
    public ConditionalItem() {
        super();
    }

    /**
     * Creates a new conditional item.
     * @param name the name value to use
     * @param description the description text to use
     * @param cost the numeric value for the cost
     * @param rarity the rarity
     * @param itemEffects the list of item effects
     */
    @JsonCreator
    public ConditionalItem(@JsonProperty("name") String name,
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
        return "Conditional";
    }

}
