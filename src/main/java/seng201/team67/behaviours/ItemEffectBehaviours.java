package seng201.team67.behaviours;

import seng201.team67.interfaces.ConcertModifier;
import seng201.team67.interfaces.StatModifier;

/**
 * Provides reusable item effect behaviours. This class holds the behaviour for the
 * different item effects in the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ItemEffectBehaviours {

    private ItemEffectBehaviours() {
    }

    /**
     * Gives a stamina boost if it below the threshold
     * @param threshold the threshold value used by the calculation
     * @return The stat modifier.
     */
    public static StatModifier lowStaminaBoost(int threshold)
    {
        return (artist, value) -> artist.getStamina() < threshold ? (int) Math.round(value) : 0;
    }

    /**
     * if the artist's star power is above a certain threshold, it boosts their stamina
     * @param threshold the threshold value used by the calculation
     * @return The stat modifier.
     */
    public static StatModifier highStarPowerStaminaBoost(int threshold)
    {
        return (artist, value) -> artist.getStarPower() > threshold ? (int) Math.round(value) : 0;
    }

    /**
     * Gives an instant boost to the artist's passed in statistic
     * @return The stat modifier.
     */
    public static StatModifier instantBoost()
    {
        return (artist, value) -> (int) Math.round(value);
    }

    /**
     * Restores an artist's stamina fully up to the base
     * @return The stat modifier.
     */
    public static StatModifier restoreUpToBase()
    {
        return (artist, value) -> Math.min(
                (int) Math.round(value),
                artist.getBaseStamina() - artist.getCurrentStaminaValue()
        );
    }

    /**
     * Gives a flat boost to the stat
     * @return The stat modifier.
     */
    public static StatModifier flatBoost()
    {
        // Always gives the same bonus while the item is equipped.
        return (artist, value) -> (int) Math.round(value);
    }

    /**
     * Converts a multiplier such as 1.15 into an additive bonus on base stamina.
     * @return The stat modifier.
     */
    public static StatModifier staminaMultiplier()
    {
        return (artist, value) -> (int) Math.round(artist.getBaseStamina() * (value));
    }

    //Concert Modifiers

    /**
     * Gives the crowd meter a flat boost
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
     * Multiplies the crowd meter by a certain amount
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
     * Multiplies the crowd meter if the player is on a win streak at or above the threshold
     * @param requiredStreak The required streak of GREAT, GOOD outcomes
     * @param multiplier The crowd meter multiplier
     * @return The concert modifier
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
     * Gives a crowd meter boost if the artist's stamina is below a certain threshold after an event.
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
     * Processes the crowd boost if the lineup is full.
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
     * Processes the crowd and stamina boost if the first event is won.
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
     * Restores all artist's stamina in the lineup if the lowest artist's stamina is below a certain amount
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
