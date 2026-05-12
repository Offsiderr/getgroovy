package seng201.team67.behaviours;

import seng201.team67.interfaces.StatModifier;

public class EffectBehavioursConsumable {

    //TODO: Evaluate other structures to see if they are more efficent

    public static StatModifier instantBoost()
    {
        //Applies a one-off flat bonus when the consumable is used
        return (artist, value) -> (int) Math.round(value);
    }

    public static StatModifier restoreUpToBase()
    {
        //Restores only the stamina missing from the artist, capped by the item value
        return (artist, value) -> Math.min(
                (int) Math.round(value),
                artist.getBaseStamina() - artist.getCurrentStaminaValue()
        );
    }
}
