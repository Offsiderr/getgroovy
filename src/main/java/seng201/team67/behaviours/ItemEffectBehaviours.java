package seng201.team67.behaviours;

import seng201.team67.interfaces.ConcertModifier;
import seng201.team67.interfaces.StatModifier;

public class ItemEffectBehaviours {

    private ItemEffectBehaviours() {
    }

    public static StatModifier lowStaminaBoost(int threshold)
    {
        // Boosts a stat if stamina drops below the threshold.
        return (artist, value) -> artist.getStamina() < threshold ? (int) Math.round(value) : 0;
    }

    public static StatModifier highStarPowerStaminaBoost(int threshold)
    {
        // Boosts stamina if star power is above the threshold.
        return (artist, value) -> artist.getStarPower() > threshold ? (int) Math.round(value) : 0;
    }

    public static StatModifier instantBoost()
    {
        // Applies a one-off flat bonus when the item is used.
        return (artist, value) -> (int) Math.round(value);
    }

    public static StatModifier restoreUpToBase()
    {
        // Restores only the missing stamina, capped by the item value.
        return (artist, value) -> Math.min(
                (int) Math.round(value),
                artist.getBaseStamina() - artist.getCurrentStaminaValue()
        );
    }

    public static StatModifier flatBoost()
    {
        // Always gives the same bonus while the item is equipped.
        return (artist, value) -> (int) Math.round(value);
    }

    public static StatModifier starPowerMultiplier()
    {
        // Converts a multiplier such as 1.25 into an additive bonus on the base stat.
        return (artist, value) -> (int) Math.round(artist.getBaseStarPowerValue() * (value - 1.0));
    }

    public static StatModifier staminaMultiplier()
    {
        // Converts a multiplier such as 1.15 into an additive bonus on base stamina.
        return (artist, value) -> (int) Math.round(artist.getBaseStamina() * (value - 1.0));
    }

    //Concert Modifiers

    public static ConcertModifier flatCrowdBoost(double amount)
    {
        return concertService -> {
            concertService.addCrowdEnergy(amount);
            concertService.markConcertModifierTriggered();
        };
    }

    public static ConcertModifier crowdMultiplier(double multiplier)
    {
        return concertService -> {
            concertService.setCrowdEnergy((int) Math.round(concertService.getCrowdEnergy() * multiplier));
            concertService.markConcertModifierTriggered();
        };
    }

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
