package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import seng201.team67.interfaces.Purchasable;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.items.Effect;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Item implements Purchasable {

    private String name;
    private String description;

    private int cost;
    private String imagePath;
    private Rarity rarity;
    private Double multiplier;
    private ArrayList<Effect> effects;

    private Boolean owned;

    protected Item() {
    }

    public Item(String name, String description, int cost, Rarity rarity, List<Effect> effects)
    {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.rarity = rarity;
        this.effects = effects == null ? new ArrayList<>() : new ArrayList<>(effects);
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

    public ArrayList<Effect> getEffects() {
        return effects;
    }


    //Getters
    @Override
    public double getCost() {
        return cost;
    }

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
