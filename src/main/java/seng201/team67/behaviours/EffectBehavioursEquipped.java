package seng201.team67.behaviours;

import seng201.team67.interfaces.StatModifier;

public class EffectBehavioursEquipped {

    //TODO: Evaluate other structures

    public static StatModifier flatBoost(int boost)
    {
        //Always gives the same bonus while the item is equipped
        return artist -> boost;
    }
}
