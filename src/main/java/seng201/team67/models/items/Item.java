package seng201.team67.models.items;

import seng201.team67.interfaces.Purchasable;
import seng201.team67.interfaces.Usable;
import seng201.team67.models.enums.Rarity;

public abstract class Item implements Purchasable {

    private int cost;
    private String imagePath;
    private Rarity rarity;
    private Double multiplier;

    public Item(int cost )
    {
        this.cost = cost;
    }

    public Double getMultiplier()
    {
        return multiplier;
    }

    public Rarity getRarity()
    {
        return rarity;
    }

    public String imagePath()
    {
        return imagePath;
    }


    //Getters
    @Override
    public double getCost() {
        return 0;
    }

}
