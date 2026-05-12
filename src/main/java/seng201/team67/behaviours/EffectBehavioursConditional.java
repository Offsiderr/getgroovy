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


    public static StatModifier lowStaminaBoost(int threshold)
    {
        //Boost stamina if it drops below the threshold
        return (artist, value) -> artist.getStamina() < threshold ? (int) Math.round(value) : 0;
    }

    public static StatModifier highStarPowerStaminaBoost(int threshold)
    {
        //Boost stamina if star power is higher than threshold
        return (artist, value) -> artist.getStarPower() > threshold ? (int) Math.round(value) : 0;
    }

    public static StatModifier lowHealthBoost(int threshold)
    {
        //Boosts a stat if the artist's health drops below the threshold
        return (artist, value) -> artist.getHealth() < threshold ? (int) Math.round(value) : 0;
    }

    public static StatModifier highStaminaBoost(int threshold)
    {
        //Boosts a stat if the artist still has plenty of stamina left
        return (artist, value) -> artist.getStamina() > threshold ? (int) Math.round(value) : 0;
    }

}
