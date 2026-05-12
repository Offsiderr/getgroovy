package seng201.team67.behaviours;

import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;

public class SkillBehaviours {

    //StatModifier factories (using builder deisgn)

    public static StatModifier flatStaminaBoost(int amount)
    {
        return (artist, value) -> amount;
    }

    public static StatModifier flatStarPowerBoost(int amount)
    {
        return (artist, value) -> amount;
    }

    public static StatModifier staminaCostReduction(double multiplier)
    {
        return (artist, value) -> (int) Math.round(multiplier * 100);
    }

    public static StatModifier retirementRisk()
    {
        return (artist, value) -> 1;
    }

    //PayoutModifier factories

    public static PayoutModifier flatCreditBonus(int amount)
    {
        return (artist, basePayout) -> basePayout + amount;
    }

    public static PayoutModifier payoutMultiplier(double multiplier)
    {
        return (artist, basePayout) -> (int) Math.round(basePayout * multiplier);
    }

    public static PayoutModifier greatPayoutMultiplier(double multiplier)
    {
        return (artist, basePayout) -> basePayout > 0 ? (int) Math.round(basePayout * multiplier) : basePayout;
    }

    public static PayoutModifier terriblePayoutReduction(double reduction)
    {
        return (artist, basePayout) -> basePayout < 0 ? (int) Math.round(basePayout * reduction) : basePayout;
    }
}
