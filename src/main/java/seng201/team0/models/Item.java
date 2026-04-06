package seng201.team0.models;

import seng201.team0.interfaces.Purchasable;
import seng201.team0.interfaces.Usable;

public class Item implements Purchasable, Usable {

    private int useAmount = 1; //How much of this item is used
    private int uses = 3; //How may uses this is has
    private int cost;

    public Item(int useAmount, int uses, int cost)
    {
        this.useAmount = useAmount;
        this.uses = uses;
        this.cost = cost;
    }


    //Getters
    @Override
    public double getCost() {
        return 0;
    }

    @Override
    public int getUses()
    {
        return uses;
    }

    @Override
    public int getUseAmount()
    {
        return useAmount;
    }

    //Setters
    public void setUse(int uses)
    {
        this.uses = uses;
    }
}
