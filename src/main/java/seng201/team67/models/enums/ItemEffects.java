package seng201.team67.models.enums;

import seng201.team67.interfaces.ConcertModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.enums.items.ItemType;
import seng201.team67.models.enums.items.StatType;

/**
 * Represents the available item effects values used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum ItemEffects {

    /** The second wind. */
    SECOND_WIND(
            "Second Wind",
            "Desperation fuels the performance.",
            Rarity.COMMON,
            StatType.STAR_POWER,
            ItemType.CONDITIONAL,
            3.0,
            GameplayEffect.SECOND_WIND
    ),

    /** The star fuelled. */
    STAR_FUELLED(
            "Star Fuelled",
            "Fame gives you energy.",
            Rarity.RARE,
            StatType.STAMINA,
            ItemType.CONDITIONAL,
            10.0,
            GameplayEffect.STAR_FUELLED
    ),

    /** The stamina boost. */
    STAMINA_BOOST("Stamina Boost",
            "A small  burst of energy",
            Rarity.COMMON,
            StatType.STAMINA,
            ItemType.CONSUMABLE,
            10.0,
            GameplayEffect.STAMINA_BOOST
    ),

    /** The star power boost. */
    STAR_POWER_BOOST(
            "Star Power Boost",
            "A short-lived boost in confidence.",
            Rarity.RARE,
            StatType.STAR_POWER,
            ItemType.CONSUMABLE,
            2.0,
            GameplayEffect.STAR_POWER_BOOST
    ),
    /** The stamina multiplier. */
    STAMINA_MULTIPLIER(
            "Stamina Multiplier",
            "Improves stamina while equipped.",
            Rarity.RARE,
            StatType.STAMINA,
            ItemType.EQUIPPED,
            1.20,
            GameplayEffect.STAMINA_MULTIPLIER
    ),

    /** The crowd boost. */
    CROWD_BOOST(
            "Crowd Boost",
            "Boosts crowd energy.",
            Rarity.COMMON,
            null,
            ItemType.CONSUMABLE,
            10.0,
            GameplayEffect.CROWD_BOOST
    ),

    /** The crowd multiplier. */
    CROWD_MULTIPLIER(
            "Crowd Multiplier",
            "Improves crowd energy gain.",
            Rarity.COMMON,
            null,
            ItemType.EQUIPPED,
            1.05,
            GameplayEffect.CROWD_MULTIPLIER
    ),

    /** The crowd boost if stamina below 20 after event 4. */
    CROWD_BOOST_IF_STAMINA_BELOW_20_AFTER_EVENT_4(
            "Crowd Boost If Stamina Below 20 After Event 4",
            "Conditional crowd boost.",
            Rarity.COMMON,
            null,
            ItemType.CONDITIONAL,
            15.0,
            GameplayEffect.CROWD_BOOST_IF_STAMINA_BELOW_20_AFTER_EVENT_4
    ),

    /** The crowd boost per event if lineup full. */
    CROWD_BOOST_PER_EVENT_IF_LINEUP_FULL(
            "Crowd Boost Per Event If Lineup Full",
            "Conditional crowd boost when the lineup is full.",
            Rarity.COMMON,
            null,
            ItemType.CONDITIONAL,
            5.0,
            GameplayEffect.CROWD_BOOST_PER_EVENT_IF_LINEUP_FULL
    ),

    /** The crowd and stamina boost if first event win. */
    CROWD_AND_STAMINA_BOOST_IF_FIRST_EVENT_WIN(
            "Crowd And Stamina Boost If First Event Win",
            "Conditional crowd and stamina boost.",
            Rarity.COMMON,
            null,
            ItemType.CONDITIONAL,
            25.0,
            GameplayEffect.CROWD_AND_STAMINA_BOOST_IF_FIRST_EVENT_WIN
    ),

    /** The crowd 2 x if three win streak. */
    CROWD_2X_IF_THREE_WIN_STREAK(
            "Crowd 2x If Three Win Streak",
            "Conditional crowd multiplier.",
            Rarity.RARE,
            null,
            ItemType.CONDITIONAL,
            2.0,
            GameplayEffect.CROWD_2X_IF_THREE_WIN_STREAK
    ),

    /** The stamina recover if below 25. */
    STAMINA_RECOVER_IF_BELOW_25(
            "Stamina Recover If Below 25",
            "Recovers stamina when it drops too low.",
            Rarity.RARE,
            StatType.STAMINA,
            ItemType.CONDITIONAL,
            30.0,
            GameplayEffect.STAMINA_RECOVER_IF_BELOW_25
    ),

    /** Auto win if crowd below 20 after event 3. */
    AUTO_WIN_IF_CROWD_BELOW_20_AFTER_EVENT_3(
            "Auto Win If Crowd Below 20 After Event 3",
            "Auto-wins the next event if the crowd is struggling.",
            Rarity.RARE,
            null,
            ItemType.CONDITIONAL,
            1.0,
            GameplayEffect.AUTO_WIN_IF_CROWD_BELOW_20_AFTER_EVENT_3
    ),

    /** Stamina recover all if lowest below 35. */
    STAMINA_RECOVER_ALL_IF_LOWEST_BELOW_35(
            "Stamina Recover All If Lowest Below 35",
            "Restores stamina to the full lineup when one artist is struggling.",
            Rarity.VERY_RARE,
            null,
            ItemType.CONDITIONAL,
            25.0,
            GameplayEffect.STAMINA_RECOVER_ALL_IF_LOWEST_BELOW_35
    ),

    /** The ticket sales 2 x if crowd above 70 before final. */
    TICKET_SALES_2X_IF_CROWD_ABOVE_70_BEFORE_FINAL(
            "Ticket Sales 2x If Crowd Above 70 Before Final",
            "Doubles ticket sales late in a strong concert.",
            Rarity.VERY_RARE,
            null,
            ItemType.CONDITIONAL,
            2.0,
            GameplayEffect.TICKET_SALES_2X_IF_CROWD_ABOVE_70_BEFORE_FINAL
    ),

    /** The crowd boost on final event. */
    CROWD_BOOST_ON_FINAL_EVENT(
            "Crowd Boost On Final Event",
            "Boosts crowd energy during the final event.",
            Rarity.VERY_RARE,
            null,
            ItemType.CONDITIONAL,
            50.0,
            GameplayEffect.CROWD_BOOST_ON_FINAL_EVENT
    ),

    /** The full roster income 2 x. */
    FULL_ROSTER_INCOME_2X(
            "Full Roster Income 2x",
            "Doubles income when every artist is fully equipped.",
            Rarity.ULTRA,
            null,
            ItemType.CONDITIONAL,
            2.0,
            GameplayEffect.FULL_ROSTER_INCOME_2X
    ),

    /** The final win income 2 x. */
    FINAL_WIN_INCOME_2X(
            "Final Win Income 2x",
            "Doubles income after winning the final event.",
            Rarity.MYTHIC,
            null,
            ItemType.CONDITIONAL,
            2.0,
            GameplayEffect.FINAL_WIN_INCOME_2X
    );

    /** Text value for the name. */
    private final String name;
    /** Text value for the description. */
    private final String description;
    /** The rarity. */
    private final Rarity rarity;
    /** The target stat. */
    private final StatType targetStat;
    /** The item type. */
    private final ItemType itemType;
    /** Numeric value for the default value. */
    private final double defaultValue;
    /** The gameplay effect. */
    private final GameplayEffect gameplayEffect;

    /**
     * Creates a new item effects.
     * It initializes the state needed for the surrounding game flow.
     * @param name the name value to use
     * @param description the description text to use
     * @param rarity the rarity
     * @param targetStat the target stat
     * @param itemType the item type
     * @param defaultValue the numeric value for the default value
     * @param gameplayEffect the gameplay effect
     */
    ItemEffects(String name, String description, Rarity rarity,
                StatType targetStat, ItemType itemType, double defaultValue, GameplayEffect gameplayEffect) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.targetStat = targetStat;
        this.itemType = itemType;
        this.defaultValue = defaultValue;
        this.gameplayEffect = gameplayEffect;
    }

    /**
     * Processes the to string.
     * @return The to string.
     */
    public String toString()
    {
        return name;
    }

    /**
     * Returns the name.
     * @return The name.
     */
    public String getName() { return name; }
    /**
     * Returns the description.
     * @return The description.
     */
    public String getDescription() { return description; }
    /**
     * Returns the rarity.
     * @return The rarity.
     */
    public Rarity getRarity() { return rarity; }
    /**
     * Returns the target stat.
     * @return The target stat.
     */
    public StatType getTargetStat() { return targetStat; }
    /**
     * Returns the item type.
     * @return The item type.
     */
    public ItemType getItemType() { return itemType; }
    /**
     * Returns the default value.
     * @return The default value.
     */
    public double getDefaultValue() { return defaultValue; }
    /**
     * Returns the gameplay effect.
     * @return The gameplay effect.
     */
    public GameplayEffect getGameplayEffect() { return gameplayEffect; }
    /**
     * Returns the stat modifier.
     * @return The stat modifier.
     */
    public StatModifier getStatModifier() { return gameplayEffect.createStatModifier(defaultValue); }
    /**
     * Returns the concert modifier.
     * @return The concert modifier.
     */
    public ConcertModifier getConcertModifier() { return gameplayEffect.createConcertModifier(defaultValue); }
}
