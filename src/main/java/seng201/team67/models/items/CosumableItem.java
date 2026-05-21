package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.interfaces.Usable;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.ItemEffects;

import java.util.List;

/**
 * The consumable item type. This means it can be used a limited amount of times by the artist but whenever they'd like during a concert
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class CosumableItem extends Item implements Usable {

    /** Numeric value for the concert uses. How many times it can be used. */
    private int concert_uses;
    /** Numeric value for the initial uses. */
    private int initialUses;

    /**
     * Creates a new cosumable item.
     */
    public CosumableItem() {
        super();
    }

    /**
     * Creates a new cosumable item.
     * @param name the name value to use
     * @param description the description text to use
     * @param uses the numeric value for the uses
     * @param cost the numeric value for the cost
     * @param rarity the rarity
     * @param itemEffects the list of item effects
     */
    @JsonCreator
    public CosumableItem(@JsonProperty("name") String name,
                         @JsonProperty("description") String description,
                         @JsonProperty("uses") int uses,
                         @JsonProperty("cost") int cost,
                         @JsonProperty("rarity") Rarity rarity,
                         @JsonProperty("effects") @JsonAlias("effect") List<ItemEffects> itemEffects) {
        super(name, description, cost, rarity, itemEffects);
        this.concert_uses = uses;
        this.initialUses = uses;
    }

    /**
     * Returns the uses.
     * @return The uses.
     */
    @Override
    public int getUses() {
        return concert_uses;
    }

    /**
     * Returns the use amount.
     * @return The use amount.
     */
    @Override
    public int getUseAmount() {
        return 1;
    }

    /**
     * Returns the initial uses.
     * @return The initial uses.
     */
    public int getInitialUses() {
        return initialUses;
    }

    /**
     * Processes the consume use.
     */
    public void consumeUse()
    {
        concert_uses = Math.max(0, concert_uses - getUseAmount());
    }

    /**
     * Returns the type.
     * @return The type.
     */
    public String getType()
    {
        return "Consumable";
    }
}
