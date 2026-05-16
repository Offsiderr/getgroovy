package seng201.team67.behaviours;

import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.questionmodels.Outcome;

import java.util.List;

public class SkillEffectBehaviours {


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


    public static PayoutModifier flatCreditBonus(int amount)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                basePayout == 0 ? basePayout : basePayout + amount;
    }

    public static PayoutModifier payoutMultiplier(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                basePayout == 0 ? basePayout : (int) Math.round(basePayout * multiplier);
    }

    public static PayoutModifier greatPayoutMultiplier(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                isOutcomeType(outcome, "GREAT") && basePayout > 0
                ? (int) Math.round(basePayout * multiplier)
                : basePayout;
    }

    public static PayoutModifier terriblePayoutReduction(double reduction)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                isOutcomeType(outcome, "TERRIBLE") && basePayout < 0
                ? (int) Math.round(basePayout * reduction)
                : basePayout;
    }

    public static PayoutModifier okPayoutMultiplier(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                isOutcomeType(outcome, "OK") && basePayout > 0
                ? (int) Math.round(basePayout * multiplier)
                : basePayout;
    }

    public static PayoutModifier headlinerBonus(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                lineup.size() == 1 && basePayout != 0
                ? (int) Math.round(basePayout * multiplier)
                : basePayout;
    }

    public static PayoutModifier collabBonus(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                hasMultipleArtistTypes(lineup) && basePayout != 0
                ? (int) Math.round(basePayout * multiplier)
                : basePayout;
    }

    public static PayoutModifier ampItUpBonus(double multiplierStep)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) -> {
            if (basePayout == 0)
            {
                return 0;
            }
            double totalMultiplier = 1.0 + (completedConcerts * (multiplierStep - 1.0));
            return (int) Math.round(basePayout * totalMultiplier);
        };
    }

    public static PayoutModifier encoreMachineBonus(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                outcome != null
                && outcome.getConcertEnds()
                && crowdEnergy >= 75
                && basePayout > 0
                ? (int) Math.round(basePayout * multiplier)
                : basePayout;
    }

    private static boolean isOutcomeType(Outcome outcome, String typeName)
    {
        return outcome != null && outcome.getOutcomeType().name().equals(typeName);
    }

    private static boolean hasMultipleArtistTypes(List<?> lineup)
    {
        return lineup.stream()
                .map(artist -> artist.getClass().getSimpleName())
                .distinct()
                .count() > 1;
    }
}
