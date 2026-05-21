package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import seng201.team67.interfaces.Purchasable;
import seng201.team67.models.enums.ItemEffects;
import seng201.team67.models.enums.Rarity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the item used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CosumableItem.class, name = "Consumable"),
        @JsonSubTypes.Type(value = CosumableItem.class, name = "CONSUMABLE"),
        @JsonSubTypes.Type(value = EquippedItem.class, name = "Equipable"),
        @JsonSubTypes.Type(value = EquippedItem.class, name = "EQUIPPED"),
        @JsonSubTypes.Type(value = ConditionalItem.class, name = "Conditional")
        ,@JsonSubTypes.Type(value = ConditionalItem.class, name = "CONDITIONAL")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Item implements Purchasable {

    /** Text value for the name. */
    private String name;
    /** Text value for the description. */
    private String description;

    /** Numeric value for the cost. */
    private int cost;
    /** Text value for the image path. */
    private String imagePath;
    /** The rarity. */
    private Rarity rarity;
    /** Numeric value for the multiplier. */
    private Double multiplier;
    /** Collection that stores the item effects. */
    private ArrayList<ItemEffects> itemEffects;

    /** Whether owned. */
    private Boolean owned;

    /**
     * Creates a new item.
     */
    protected Item() {
    }

    /**
     * Creates a new item.
     * @param name the name value to use
     * @param description the description text to use
     * @param cost the numeric value for the cost
     * @param rarity the rarity
     * @param itemEffects the list of item effects
     */
    public Item(String name, String description, int cost, Rarity rarity, List<ItemEffects> itemEffects)
    {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.rarity = rarity;
        this.itemEffects = itemEffects == null ? new ArrayList<>() : new ArrayList<>(itemEffects);
        owned = false;
    }

    /**
     * Returns the multiplier.
     * @return The multiplier.
     */
    public Double getMultiplier()
    {
        return multiplier;
    }

    /**
     * Returns the name.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the rarity.
     * @return The rarity.
     */
    public Rarity getRarity()
    {
        return rarity;
    }

    /**
     * Returns the image path.
     * @return The image path.
     */
    public String getImagePath()
    {
        return imagePath;
    }

    /**
     * Returns the effects.
     * @return The effects.
     */
    public ArrayList<ItemEffects> getEffects() {
        return itemEffects;
    }


    //Getters
    /**
     * Returns the cost.
     * @return The cost.
     */
    @Override
    public double getCost() {
        return cost;
    }

    /**
     * Returns the type.
     * @return The type.
     */
    @JsonIgnore
    public String getType()
    {
        return "error";
    }

    /**
     * Sets the image path.
     * @param imagePath the text value for the image path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Sets the multiplier.
     * @param multiplier the multiplier used by the calculation
     */
    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Sets the name.
     * @param name the name value to use
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description.
     * @param description the description text to use
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Processes the purchase.
     */
    public void purchase()
    {
        owned = true;
    }

    /**
     * Processes the dispose.
     */
    public void dispose()
    {
        owned = false;
    }

    /**
     * Returns the owned.
     * @return The owned.
     */
    public Boolean getOwned()
    {
        return owned;
    }

}
