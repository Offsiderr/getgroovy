package seng201.team67.behaviours;

import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.questionmodels.Outcome;

import java.util.List;

/**
 * Provides reusable skill effect behaviours behaviour definitions for the game systems.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class SkillEffectBehaviours {


    /**
     * Processes the flat stamina boost.
     * @param amount the amount to apply
     * @return The stat modifier.
     */
    public static StatModifier flatStaminaBoost(int amount)
    {
        return (artist, value) -> scaleFlatEffect(amount, artist.getSkillLevel());
    }

    /**
     * Processes the flat star power boost.
     * @param amount the amount to apply
     * @return The stat modifier.
     */
    public static StatModifier flatStarPowerBoost(int amount)
    {
        return (artist, value) -> scaleFlatEffect(amount, artist.getSkillLevel());
    }

    /**
     * Processes the stamina cost reduction.
     * @param multiplier the multiplier used by the calculation
     * @return The stat modifier.
     */
    public static StatModifier staminaCostReduction(double multiplier)
    {
        return (artist, value) -> (int) Math.round(scaleMultiplierEffect(multiplier, artist.getSkillLevel()) * 100);
    }

    /**
     * Processes the retirement risk.
     * @return The stat modifier.
     */
    public static StatModifier retirementRisk()
    {
        return (artist, value) -> 1;
    }


    /**
     * Processes the flat credit bonus.
     * @param amount the amount to apply
     * @return The payout modifier.
     */
    public static PayoutModifier flatCreditBonus(int amount)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                basePayout == 0 ? basePayout : basePayout + amount;
    }

    /**
     * Processes the payout multiplier.
     * @param multiplier the multiplier used by the calculation
     * @return The payout modifier.
     */
    public static PayoutModifier payoutMultiplier(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                basePayout > 0 ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    /**
     * Processes the great payout multiplier.
     * It updates related state as needed while performing the operation.
     * @param multiplier the multiplier used by the calculation
     * @return The payout modifier.
     */
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

    /**
     * Processes the terrible payout reduction.
     * @param reduction the numeric value for the reduction
     * @return The payout modifier.
     */
    public static PayoutModifier terriblePayoutReduction(double reduction)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) -> {
            boolean isBadOrTerrible = isOutcomeType(outcome, "TERRIBLE") || isOutcomeType(outcome, "BAD");
            if (isBadOrTerrible && basePayout < 0)
                return (int) Math.round(basePayout * Math.min(1.0, scaleMultiplierEffect(reduction, artist.getSkillLevel())));
            return basePayout;
        };
    }

    /**
     * Processes the ok payout multiplier.
     * @param multiplier the multiplier used by the calculation
     * @return The payout modifier.
     */
    public static PayoutModifier okPayoutMultiplier(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                isOutcomeType(outcome, "OK") && basePayout > 0
                        ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    /**
     * Processes the headliner bonus.
     * @param multiplier the multiplier used by the calculation
     * @return The payout modifier.
     */
    public static PayoutModifier headlinerBonus(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                lineup.size() == 1 && basePayout != 0
                        ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    /**
     * Processes the collab bonus.
     * @param multiplier the multiplier used by the calculation
     * @return The payout modifier.
     */
    public static PayoutModifier collabBonus(double multiplier)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                hasMultipleArtistTypes(lineup) && basePayout != 0
                        ? (int) Math.round(basePayout * scaleMultiplierEffect(multiplier, artist.getSkillLevel()))
                        : basePayout;
    }

    /**
     * Processes the amp it up bonus.
     * It updates related state as needed while performing the operation.
     * @param multiplierStep the numeric value for the multiplier step
     * @return The payout modifier.
     */
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

    /**
     * Processes the encore machine bonus.
     * It updates related state as needed while performing the operation.
     * @param multiplier the multiplier used by the calculation
     * @return The payout modifier.
     */
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