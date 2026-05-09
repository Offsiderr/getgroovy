package seng201.team67.behaviours;

import seng201.team67.interfaces.StatModifier;

public class EffectBehavioursConsumable {

    //TODO: Evaluate other structures to see if they are more efficent

    public static StatModifier instantBoost(int boost)
    {
        //Applies a one-off flat bonus when the consumable is used
        return artist -> boost;
    }

    public static StatModifier restoreUpToBase(int restoreAmount)
    {
        //Restores only the stamina missing from the artist, capped by the item value
        return artist -> Math.min(restoreAmount, artist.getBaseStamina() - artist.getStamina());
    }
}
