package seng201.team67.behaviours;

import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.questionmodels.Outcome;

import java.util.List;

public class SkillEffectBehaviours {


    public static StatModifier flatStaminaBoost(int amount)
    {
        return (artist, value) -> scaleFlatEffect(amount, artist.getSkillLevel());
    }

    public static StatModifier flatStarPowerBoost(int amount)
    {
        return (artist, value) -> scaleFlatEffect(amount, artist.getSkillLevel());
    }

    public static StatModifier staminaCostReduction(double multiplier)
    {
        return (artist, value) -> (int) Math.round(scaleMultiplierEffect(multiplier, artist.getSkillLevel()) * 100);
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
                basePayout > 0 ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    public static PayoutModifier greatPayoutMultiplier(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) -> {
            if (isOutcomeType(outcome, "GREAT") && basePayout > 0)
                return (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()));
            if (isOutcomeType(outcome, "BAD") && basePayout < 0)
                return (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()));
            return basePayout;
        };
    }

    public static PayoutModifier terriblePayoutReduction(double reduction)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) -> {
            boolean isBadOrTerrible = isOutcomeType(outcome, "TERRIBLE") || isOutcomeType(outcome, "BAD");
            if (isBadOrTerrible && basePayout < 0)
                return (int) Math.round(basePayout * Math.min(1.0, scaleMultiplierEffect(reduction, artist.getSkillLevel())));
            return basePayout;
        };
    }

    public static PayoutModifier okPayoutMultiplier(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                isOutcomeType(outcome, "OK") && basePayout > 0
                        ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    public static PayoutModifier headlinerBonus(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                lineup.size() == 1 && basePayout != 0
                        ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    public static PayoutModifier collabBonus(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                hasMultipleArtistTypes(lineup) && basePayout != 0
                        ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    public static PayoutModifier ampItUpBonus(double multiplierStep)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) -> {
            if (basePayout == 0)
            {
                return 0;
            }
            double effectiveStep = scaleMultiplierEffect(multiplierStep, artist.getSkillLevel());
            double totalMultiplier = 1.0 + (completedConcerts * (effectiveStep - 1.0));
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
                        ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    private static int scaleFlatEffect(int amount, int skillLevel)
    {
        return amount * Math.max(1, skillLevel);
    }

    private static double scaleMultiplierEffect(double multiplier, int skillLevel)
    {
        double scaledMultiplier = 1.0 + ((multiplier - 1.0) * Math.max(1, skillLevel));
        return Math.max(0.0, scaledMultiplier);
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