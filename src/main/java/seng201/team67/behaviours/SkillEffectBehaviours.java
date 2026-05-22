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
     * Boosts an artist's stamina by a flat amount, scaled by their skill level.
     * @param amount the amount to apply
     * @return The stat modifier.
     */
    public static StatModifier flatStaminaBoost(int amount)
    {
        return (artist, value) -> scaleFlatEffect(amount, artist.getSkillLevel());
    }

    /**
     * Reduces the stamina cost of an action by a percentage, scaled by skill level.
     * Returns the cost as an integer percentage eg. 80 = 20% reduction
     * @param amount the amount to apply
     * @return The stat modifier.
     */
    public static StatModifier flatStarPowerBoost(int amount)
    {
        return (artist, value) -> scaleFlatEffect(amount, artist.getSkillLevel());
    }

    /**
     * Applies a payout bonus only when the concert ends with high crowd energy >= 75
     * @param multiplier the multiplier used by the calculation
     * @return The stat modifier.
     */
    public static StatModifier staminaCostReduction(double multiplier)
    {
        return (artist, value) -> (int) Math.round(scaleMultiplierEffect(multiplier, artist.getSkillLevel()) * 100);
    }

    /**
     * Calculates the threshold at which an artist risks retirement, 60% of their
     * current tolerance stat.
     * @return The stat modifier.
     */
    public static StatModifier retirementRisk()
    {
        return (artist, value) -> (int) Math.round(artist.getTolerance() * 0.6);
    }


    /**
     * Adds a flat credit bonus to any non-zero payout, scaled by the artist's skill level.
     * Has no effect if the base payout is zero.
     * @param amount the amount to apply
     * @return The payout modifier.
     */
    public static PayoutModifier flatCreditBonus(int amount)
    {
        return (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                basePayout == 0 ? basePayout : basePayout + amount;
    }

    /**
     * Multiplies any positive payout by a skill-scaled factor. Penalties are left unchanged.
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
     * Amplifies positive payouts on GREAT outcomes and negative payouts on BAD outcomes.
     * Rewards high performance while making bad concerts hurt more — both scaled by skill level.
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
     * Softens the credit penalty on BAD or TERRIBLE concert outcomes.
     * The reduction is capped at 1.0 so it can never flip a penalty into a reward.
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
     * Boosts the payout for OK-rated concerts, rewarding consistency even without a standout performance.
     * Has no effect on GREAT, BAD, or TERRIBLE outcomes.
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
     * Grants a bonus when the artist is the sole performer in the lineup.
     * Applies to both positive and negative payouts — going it alone carries risk too.
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
     * Rewards lineups featuring more than one artist type: rap, pop, rock, etc.
     * Applies to both positive and negative payouts.
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
     * Grows the payout multiplier with each completed concert in the tour.
     * Each additional concert compounds the step bonus, so later shows pay out
     * progressively more
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
     * Grants a bonus when the concert ends on a high note — crowd energy at or above 75
     * and the outcome marks the concert as finished. Has no effect on zero or negative payouts.
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

    /**
     * Scales a flat effect amount by the artist's skill level, with a minimum scale of 1.
     *
     * @param amount     the base flat value
     * @param skillLevel the artist's current skill level
     */
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