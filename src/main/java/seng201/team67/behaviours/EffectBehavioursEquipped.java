package seng201.team67.behaviours;

import seng201.team67.interfaces.StatModifier;

public class EffectBehavioursEquipped {

    //TODO: Evaluate other structures

    public static StatModifier flatBoost()
    {
        //Always gives the same bonus while the item is equipped
        return (artist, value) -> (int) Math.round(value);
    }

    public static StatModifier starPowerMultiplier()
    {
        //Converts a multiplier such as 1.25 into an additive bonus on the base stat
        return (artist, value) -> (int) Math.round(artist.getBaseStarPowerValue() * (value - 1.0));
    }

    public static StatModifier staminaMultiplier()
    {
        //Converts a multiplier such as 1.15 into an additive bonus on base stamina
        return (artist, value) -> (int) Math.round(artist.getBaseStamina() * (value - 1.0));
    }
}
