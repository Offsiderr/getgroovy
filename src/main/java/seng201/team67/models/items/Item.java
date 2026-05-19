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

    private String name;
    private String description;

    private int cost;
    private String imagePath;
    private Rarity rarity;
    private Double multiplier;
    private ArrayList<ItemEffects> itemEffects;

    private Boolean owned;

    protected Item() {
    }

    public Item(String name, String description, int cost, Rarity rarity, List<ItemEffects> itemEffects)
    {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.rarity = rarity;
        this.itemEffects = itemEffects == null ? new ArrayList<>() : new ArrayList<>(itemEffects);
        owned = false;
    }

    public Double getMultiplier()
    {
        return multiplier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity()
    {
        return rarity;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public ArrayList<ItemEffects> getEffects() {
        return itemEffects;
    }


    //Getters
    @Override
    public double getCost() {
        return cost;
    }

    @JsonIgnore
    public String getType()
    {
        return "error";
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void purchase()
    {
        owned = true;
    }

    public void dispose()
    {
        owned = false;
    }

    public Boolean getOwned()
    {
        return owned;
    }

}
