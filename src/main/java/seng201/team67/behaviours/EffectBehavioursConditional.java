package seng201.team67.behaviours;

import seng201.team67.interfaces.StatModifier;

public class EffectBehavioursConditional {

    //How to create a new conditional effect!:
    //Create a new function that returns a StatModifier,
    //and return a lambda.
    //
    //lambdas work by condensing a function into one line of code.
    //
    //The lambda below takes in an artist, then checks if the stamina is below the threshold,
    //and if it is, returns bonus, and otherwise, returns 0.
    //
    //We are using these for any effects that aren't just a straight boost. eg. has conditions
    //
    //
    //PLEASE make a comment describing your stat modifier for quick reference
    //
    //ask me if you have any questions - Louie


    public static StatModifier lowStaminaBoost(int bonus, int threshold)
    {
        //Boost stamina if it drops below the threshold
        return artist -> artist.getStamina() < threshold ? bonus : 0;
    }

    public static StatModifier highStarPowerStaminaBoost(int boost, int threshold)
    {
        //Boost stamina if star power is higher than threshold
        return artist -> artist.getStarPower() > threshold ? boost : 0;
    }

    public static StatModifier lowHealthBoost(int bonus, int threshold)
    {
        //Boosts a stat if the artist's health drops below the threshold
        return artist -> artist.getHealth() < threshold ? bonus : 0;
    }

    public static StatModifier highStaminaBoost(int bonus, int threshold)
    {
        //Boosts a stat if the artist still has plenty of stamina left
        return artist -> artist.getStamina() > threshold ? bonus : 0;
    }

}
