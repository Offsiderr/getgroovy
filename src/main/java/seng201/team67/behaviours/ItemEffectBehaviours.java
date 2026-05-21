package seng201.team67.behaviours;

import seng201.team67.interfaces.ConcertModifier;
import seng201.team67.interfaces.StatModifier;

/**
 * Provides reusable item effect behaviours behaviour definitions for the game systems.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ItemEffectBehaviours {

    private ItemEffectBehaviours() {
    }

    /**
     * Processes the low stamina boost.
     * @param threshold the threshold value used by the calculation
     * @return The stat modifier.
     */
    public static StatModifier lowStaminaBoost(int threshold)
    {
        // Boosts a stat if stamina drops below the threshold.
        return (artist, value) -> artist.getStamina() < threshold ? (int) Math.round(value) : 0;
    }

    /**
     * Processes the high star power stamina boost.
     * @param threshold the threshold value used by the calculation
     * @return The stat modifier.
     */
    public static StatModifier highStarPowerStaminaBoost(int threshold)
    {
        // Boosts stamina if star power is above the threshold.
        return (artist, value) -> artist.getStarPower() > threshold ? (int) Math.round(value) : 0;
    }

    /**
     * Processes the instant boost.
     * @return The stat modifier.
     */
    public static StatModifier instantBoost()
    {
        // Applies a one-off flat bonus when the item is used.
        return (artist, value) -> (int) Math.round(value);
    }

    /**
     * Restores the up to base.
     * @return The stat modifier.
     */
    public static StatModifier restoreUpToBase()
    {
        // Restores only the missing stamina, capped by the item value.
        return (artist, value) -> Math.min(
                (int) Math.round(value),
                artist.getBaseStamina() - artist.getCurrentStaminaValue()
        );
    }

    /**
     * Processes the flat boost.
     * @return The stat modifier.
     */
    public static StatModifier flatBoost()
    {
        // Always gives the same bonus while the item is equipped.
        return (artist, value) -> (int) Math.round(value);
    }

    /**
     * Processes the star power multiplier.
     * @return The stat modifier.
     */
    public static StatModifier starPowerMultiplier()
    {
        // Converts a multiplier such as 1.25 into an additive bonus on the base stat.
        return (artist, value) -> (int) Math.round(artist.getBaseStarPowerValue() * (value - 1.0));
    }

    /**
     * Processes the stamina multiplier.
     * @return The stat modifier.
     */
    public static StatModifier staminaMultiplier()
    {
        // Converts a multiplier such as 1.15 into an additive bonus on base stamina.
        return (artist, value) -> (int) Math.round(artist.getBaseStamina() * (value));
    }

    //Concert Modifiers

    /**
     * Processes the flat crowd boost.
     * @param amount the amount to apply
     * @return The concert modifier.
     */
    public static ConcertModifier flatCrowdBoost(double amount)
    {
        return concertService -> {
            concertService.addCrowdEnergy(amount);
            concertService.markConcertModifierTriggered();
        };
    }

    /**
     * Processes the crowd multiplier.
     * @param multiplier the multiplier used by the calculation
     * @return The concert modifier.
     */
    public static ConcertModifier crowdMultiplier(double multiplier)
    {
        return concertService -> {
            concertService.setCrowdEnergy((int) Math.round(concertService.getCrowdEnergy() * multiplier));
            concertService.markConcertModifierTriggered();
        };
    }

    /**
     * Processes the crowd boost if win streak.
     * @param requiredStreak the numeric value for the required streak
     * @param multiplier the multiplier used by the calculation
     * @return The concert modifier.
     */
    public static ConcertModifier crowdBoostIfWinStreak(int requiredStreak, double multiplier)
    {
        return concertService -> {
            if (concertService.getWinStreak() >= requiredStreak)
            {
                concertService.setCrowdEnergy((int) Math.round(concertService.getCrowdEnergy() * multiplier));
                concertService.markConcertModifierTriggered();
            }
        };
    }

    /**
     * Processes the crowd boost if stamina below after event.
     * It updates related state as needed while performing the operation.
     * @param threshold the threshold value used by the calculation
     * @param eventNumberThreshold the numeric value for the event number threshold
     * @param amount the amount to apply
     * @return The concert modifier.
     */
    public static ConcertModifier crowdBoostIfStaminaBelowAfterEvent(int threshold, int eventNumberThreshold, double amount)
    {
        return concertService -> {
            if (concertService.getAnsweredQuestionCount() < eventNumberThreshold)
            {
                return;
            }

            boolean anyBelowThreshold = concertService.getLineup().stream()
                    .anyMatch(artist -> artist.getStamina() < threshold);
            if (anyBelowThreshold)
            {
                concertService.addCrowdEnergy(amount);
                concertService.markConcertModifierTriggered();
            }
        };
    }

    /**
     * Processes the crowd boost if lineup full.
     * @param fullSize the numeric value for the full size
     * @param amount the amount to apply
     * @return The concert modifier.
     */
    public static ConcertModifier crowdBoostIfLineupFull(int fullSize, double amount)
    {
        return concertService -> {
            if (concertService.getLineup().size() == fullSize)
            {
                concertService.addCrowdEnergy(amount);
                concertService.markConcertModifierTriggered();
            }
        };
    }

    /**
     * Processes the crowd and stamina boost if first event won.
     * It updates related state as needed while performing the operation.
     * @param crowdAmount the numeric value for the crowd amount
     * @param staminaAmount the numeric value for the stamina amount
     * @return The concert modifier.
     */
    public static ConcertModifier crowdAndStaminaBoostIfFirstEventWon(double crowdAmount, double staminaAmount)
    {
        return concertService -> {
            if (concertService.getAnsweredQuestionCount() == 1 && concertService.wasLastEventWon())
            {
                concertService.addCrowdEnergy(crowdAmount);
                int staminaBoost = (int) Math.round(staminaAmount);
                for (var artist : concertService.getLineup())
                {
                    artist.setStamina(artist.getCurrentStaminaValue() + staminaBoost);
                }
                concertService.markConcertModifierTriggered();
            }
        };
    }

    /**
     * Processes the crowd boost on final event.
     * @param totalEvents the numeric value for the total events
     * @param amount the amount to apply
     * @return The concert modifier.
     */
    public static ConcertModifier crowdBoostOnFinalEvent(int totalEvents, double amount)
    {
        return concertService -> {
            if (concertService.getAnsweredQuestionCount() == totalEvents)
            {
                concertService.addCrowdEnergy(amount);
                concertService.markConcertModifierTriggered();
            }
        };
    }

    /**
     * Processes the income multiplier if crowd above before final.
     * It updates related state as needed while performing the operation.
     * @param threshold the threshold value used by the calculation
     * @param multiplier the multiplier used by the calculation
     * @return The concert modifier.
     */
    public static ConcertModifier incomeMultiplierIfCrowdAboveBeforeFinal(int threshold, double multiplier)
    {
        return concertService -> {
            if (concertService.getTotalConcertEvents() > 1
                    && concertService.getAnsweredQuestionCount() == concertService.getTotalConcertEvents() - 1
                    && concertService.getCrowdEnergy() > threshold)
            {
                concertService.multiplyIncomeMultiplier(multiplier);
                concertService.markConcertModifierTriggered();
            }
        };
    }

    /**
     * Processes the income multiplier if everyone fully equipped.
     * It updates related state as needed while performing the operation.
     * @param requiredItemsPerArtist the numeric value for the required items per artist
     * @param multiplier the multiplier used by the calculation
     * @return The concert modifier.
     */
    public static ConcertModifier incomeMultiplierIfEveryoneFullyEquipped(int requiredItemsPerArtist, double multiplier)
    {
        return concertService -> {
            boolean everyoneFullyEquipped = !concertService.getLineup().isEmpty()
                    && concertService.getLineup().stream().allMatch(artist -> artist.getItems().size() >= requiredItemsPerArtist);
            if (everyoneFullyEquipped)
            {
                concertService.multiplyIncomeMultiplier(multiplier);
                concertService.markConcertModifierTriggered();
            }
        };
    }

    /**
     * Processes the income multiplier if final event won.
     * @param multiplier the multiplier used by the calculation
     * @return The concert modifier.
     */
    public static ConcertModifier incomeMultiplierIfFinalEventWon(double multiplier)
    {
        return concertService -> {
            if (concertService.wasLastEventWon() && concertService.isFinalConcertEvent())
            {
                concertService.multiplyIncomeMultiplier(multiplier);
                concertService.markConcertModifierTriggered();
            }
        };
    }

    /**
     * Processes the auto win next event if crowd below after event.
     * @param crowdThreshold the numeric value for the crowd threshold
     * @param eventNumberThreshold the numeric value for the event number threshold
     * @return The concert modifier.
     */
    public static ConcertModifier autoWinNextEventIfCrowdBelowAfterEvent(int crowdThreshold, int eventNumberThreshold)
    {
        return concertService -> {
            if (concertService.getAnsweredQuestionCount() >= eventNumberThreshold
                    && concertService.getCrowdEnergy() < crowdThreshold)
            {
                concertService.requestBestOutcomeNextEvent();
            }
        };
    }

    /**
     * Restores the all stamina if lowest below.
     * It updates related state as needed while performing the operation.
     * @param threshold the threshold value used by the calculation
     * @param amount the amount to apply
     * @return The concert modifier.
     */
    public static ConcertModifier restoreAllStaminaIfLowestBelow(int threshold, int amount)
    {
        return concertService -> {
            boolean anyBelowThreshold = concertService.getLineup().stream()
                    .anyMatch(artist -> artist.getStamina() < threshold);
            if (!anyBelowThreshold)
            {
                return;
            }

            for (var artist : concertService.getLineup())
            {
                artist.setStamina(artist.getCurrentStaminaValue() + amount);
            }
            concertService.markConcertModifierTriggered();
        };
    }
}
