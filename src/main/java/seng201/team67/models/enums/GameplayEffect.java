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
    /** The second wind. */
    SECOND_WIND {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.lowStaminaBoost(30);
        }
    },
    /** The star fuelled. */
    STAR_FUELLED {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.highStarPowerStaminaBoost(5);
        }
    },
    /** The stamina boost. */
    STAMINA_BOOST {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.restoreUpToBase();
        }
    },
    /** The star power boost. */
    STAR_POWER_BOOST {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.instantBoost();
        }
    },
    /** The star power multiplier. */
    STAR_POWER_MULTIPLIER {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.starPowerMultiplier();
        }
    },
    /** The stamina multiplier. */
    STAMINA_MULTIPLIER {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.staminaMultiplier();
        }
    },
    /** The crowd boost. */
    CROWD_BOOST {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.flatCrowdBoost(10);
        }
    },
    /** The crowd multiplier. */
    CROWD_MULTIPLIER {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdMultiplier(1.05);
        }
    },
    /** The crowd boost if stamina below 20 after event 4. */
    CROWD_BOOST_IF_STAMINA_BELOW_20_AFTER_EVENT_4 {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdBoostIfStaminaBelowAfterEvent(20, 4, 15);
        }
    },
    /** The crowd boost per event if lineup full. */
    CROWD_BOOST_PER_EVENT_IF_LINEUP_FULL {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdBoostIfLineupFull(3, 5);
        }
    },
    /** The crowd and stamina boost if first event win. */
    CROWD_AND_STAMINA_BOOST_IF_FIRST_EVENT_WIN {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdAndStaminaBoostIfFirstEventWon(25, 10);
        }
    },
    /** The crowd 2 x if three win streak. */
    CROWD_2X_IF_THREE_WIN_STREAK {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.crowdBoostIfWinStreak(3, 2.0);
        }
    },
    /** The stamina recover if below 25. */
    STAMINA_RECOVER_IF_BELOW_25 {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return ItemEffectBehaviours.lowStaminaBoost(25);
        }
    },
    /** The auto win if crowd below 20 after event 3. */
    AUTO_WIN_IF_CROWD_BELOW_20_AFTER_EVENT_3 {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.autoWinNextEventIfCrowdBelowAfterEvent(20, 3);
        }
    },
    /** The stamina recover all if lowest below 35. */
    STAMINA_RECOVER_ALL_IF_LOWEST_BELOW_35 {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.restoreAllStaminaIfLowestBelow(35, 25);
        }
    },
    /** The ticket sales 2 x if crowd above 70 before final. */
    TICKET_SALES_2X_IF_CROWD_ABOVE_70_BEFORE_FINAL {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.incomeMultiplierIfCrowdAboveBeforeFinal(70, 2.0);
        }
    },
    /** The crowd boost on final event. */
    CROWD_BOOST_ON_FINAL_EVENT {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return concertService -> ItemEffectBehaviours
                    .crowdBoostOnFinalEvent(concertService.getTotalConcertEvents(), 50)
                    .apply(concertService);
        }
    },
    /** The full roster income 2 x. */
    FULL_ROSTER_INCOME_2X {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.incomeMultiplierIfEveryoneFullyEquipped(3, 2.0);
        }
    },
    /** The final win income 2 x. */
    FINAL_WIN_INCOME_2X {
        @Override
        public ConcertModifier createConcertModifier(double value)
        {
            return ItemEffectBehaviours.incomeMultiplierIfFinalEventWon(2.0);
        }
    },
    /** The flat stamina boost. */
    FLAT_STAMINA_BOOST {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.flatStaminaBoost((int) value);
        }
    },
    /** The flat star power boost. */
    FLAT_STAR_POWER_BOOST {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.flatStarPowerBoost((int) value);
        }
    },
    /** The stamina cost reduction. */
    STAMINA_COST_REDUCTION {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.staminaCostReduction(value);
        }
    },
    /** The battle hardened. */
    BATTLE_HARDENED {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.staminaCostReduction(value);
        }
    },
    /** The retirement risk. */
    RETIREMENT_RISK {
        @Override
        public StatModifier createStatModifier(double value)
        {
            return SkillEffectBehaviours.retirementRisk();
        }
    },
    /** The flat credit bonus. */
    FLAT_CREDIT_BONUS {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.flatCreditBonus((int) value);
        }
    },
    /** The payout multiplier. */
    PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.payoutMultiplier(value);
        }
    },
    /** The ok payout multiplier. */
    OK_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.okPayoutMultiplier(value);
        }
    },
    /** The great payout multiplier. */
    GREAT_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.greatPayoutMultiplier(value);
        }
    },
    /** The terrible payout reduction. */
    TERRIBLE_PAYOUT_REDUCTION {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.terriblePayoutReduction(value);
        }
    },
    /** The solo payout multiplier. */
    SOLO_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.headlinerBonus(value);
        }
    },
    /** The collab payout multiplier. */
    COLLAB_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.collabBonus(value);
        }
    },
    /** The encore payout multiplier. */
    ENCORE_PAYOUT_MULTIPLIER {
        @Override
        public PayoutModifier createPayoutModifier(double value)
        {
            return SkillEffectBehaviours.encoreMachineBonus(value);
        }
    },
    /** The tour progress payout multiplier. */
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