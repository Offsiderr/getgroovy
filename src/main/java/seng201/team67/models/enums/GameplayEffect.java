package seng201.team67.models.enums;

import seng201.team67.behaviours.ItemEffectBehaviours;
import seng201.team67.behaviours.SkillEffectBehaviours;
import seng201.team67.interfaces.ConcertModifier;
import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;

/**
 * Represents the available gameplay effect values used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum GameplayEffect {

    SECOND_WIND {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.lowStaminaBoost(30);
        }
    },

    STAR_FUELLED {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.highStarPowerStaminaBoost(5);
        }
    },

    STAMINA_BOOST {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.restoreUpToBase();
        }
    },

    STAR_POWER_BOOST {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.instantBoost();
        }
    },

    STAR_POWER_MULTIPLIER {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.starPowerMultiplier();
        }
    },

    STAMINA_MULTIPLIER {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.staminaMultiplier();
        }
    },

    CROWD_BOOST {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.flatCrowdBoost(10);
        }
    },

    CROWD_MULTIPLIER {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdMultiplier(1.05);
        }
    },

    CROWD_BOOST_IF_STAMINA_BELOW_20_AFTER_EVENT_4 {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdBoostIfStaminaBelowAfterEvent(20, 4, 15);
        }
    },

    CROWD_BOOST_PER_EVENT_IF_LINEUP_FULL {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdBoostIfLineupFull(3, 5);
        }
    },

    CROWD_AND_STAMINA_BOOST_IF_FIRST_EVENT_WIN {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdAndStaminaBoostIfFirstEventWon(25, 10);
        }
    },
    CROWD_2X_IF_THREE_WIN_STREAK {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdBoostIfWinStreak(3, 2.0);
        }
    },
    STAMINA_RECOVER_IF_BELOW_25 {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.lowStaminaBoost(25);
        }
    },
    AUTO_WIN_IF_CROWD_BELOW_20_AFTER_EVENT_3 {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.autoWinNextEventIfCrowdBelowAfterEvent(20, 3);
        }
    },
    STAMINA_RECOVER_ALL_IF_LOWEST_BELOW_35 {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.restoreAllStaminaIfLowestBelow(35, 25);
        }
    },
    TICKET_SALES_2X_IF_CROWD_ABOVE_70_BEFORE_FINAL {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.incomeMultiplierIfCrowdAboveBeforeFinal(70, 2.0);
        }
    },
    CROWD_BOOST_ON_FINAL_EVENT {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return concertService -> ItemEffectBehaviours
                    .crowdBoostOnFinalEvent(concertService.getTotalConcertEvents(), 50)
                    .apply(concertService);
        }
    },
    FULL_ROSTER_INCOME_2X {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.incomeMultiplierIfEveryoneFullyEquipped(3, 2.0);
        }
    },
    FINAL_WIN_INCOME_2X {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.incomeMultiplierIfFinalEventWon(2.0);
        }
    },

    FLAT_STAMINA_BOOST {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.flatStaminaBoost((int) value);
        }
    },

    FLAT_STAR_POWER_BOOST {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.flatStarPowerBoost((int) value);
        }
    },

    STAMINA_COST_REDUCTION {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.staminaCostReduction(value);
        }
    },

    BATTLE_HARDENED {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.staminaCostReduction(value);
        }
    },

    RETIREMENT_RISK {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.retirementRisk();
        }
    },

    FLAT_CREDIT_BONUS {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.flatCreditBonus((int) value);
        }
    },

    PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.payoutMultiplier(value);
        }
    },

    OK_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.okPayoutMultiplier(value);
        }
    },

    GREAT_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.greatPayoutMultiplier(value);
        }
    },

    TERRIBLE_PAYOUT_REDUCTION {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.terriblePayoutReduction(value);
        }
    },

    SOLO_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.headlinerBonus(value);
        }
    },

    COLLAB_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.collabBonus(value);
        }
    },

    ENCORE_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.encoreMachineBonus(value);
        }
    },

    TOUR_PROGRESS_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.ampItUpBonus(value);
        }
    };

    /**
     * Creates the stat modifier.
     * @param value the numeric value for the value
     * @return The resulting stat modifier, or `null` if no value is available.
     */
    public StatModifier createStatModifier(double value)
    {
        return null;
    }

    /**
     * Creates the payout modifier.
     * @param value the numeric value for the value
     * @return The resulting payout modifier, or `null` if no value is available.
     */
    public PayoutModifier createPayoutModifier(double value)
    {
        return null;
    }

    /**
     * Creates the concert modifier.
     * @param value the numeric value for the value
     * @return The resulting concert modifier, or `null` if no value is available.
     */
    public ConcertModifier createConcertModifier(double value)
    {
        return null;
    }
}