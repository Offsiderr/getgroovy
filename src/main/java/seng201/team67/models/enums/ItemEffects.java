package seng201.team67.models.enums;

import seng201.team67.interfaces.ConcertModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.enums.items.ItemType;
import seng201.team67.models.enums.items.StatType;

public enum ItemEffects {

    //Conditional
    SECOND_WIND(
            "Second Wind",
            "Desperation fuels the performance.",
            Rarity.COMMON,
            StatType.STAR_POWER,
            ItemType.CONDITIONAL,
            3.0,
            GameplayEffect.SECOND_WIND
    ),

    STAR_FUELLED(
            "Star Fuelled",
            "Fame gives you energy.",
            Rarity.RARE,
            StatType.STAMINA,
            ItemType.CONDITIONAL,
            10.0,
            GameplayEffect.STAR_FUELLED
    ),

    STAMINA_BOOST("Stamina Boost",
            "A small  burst of energy",
            Rarity.COMMON,
            StatType.STAMINA,
            ItemType.CONSUMABLE,
            10.0,
            GameplayEffect.STAMINA_BOOST
    ),

    STAR_POWER_BOOST(
            "Star Power Boost",
            "A short-lived boost in confidence.",
            Rarity.RARE,
            StatType.STAR_POWER,
            ItemType.CONSUMABLE,
            2.0,
            GameplayEffect.STAR_POWER_BOOST
    ),

    STAR_POWER_MULTIPLIER(
            "Star Power Multiplier",
            "Improves star power while equipped.",
            Rarity.COMMON,
            StatType.STAR_POWER,
            ItemType.EQUIPPED,
            1.05,
            GameplayEffect.STAR_POWER_MULTIPLIER
    ),

    STAMINA_MULTIPLIER(
            "Stamina Multiplier",
            "Improves stamina while equipped.",
            Rarity.RARE,
            StatType.STAMINA,
            ItemType.EQUIPPED,
            1.1,
            GameplayEffect.STAMINA_MULTIPLIER
    ),

    CROWD_BOOST(
            "Crowd Boost",
            "Boosts crowd energy.",
            Rarity.COMMON,
            null,
            ItemType.CONSUMABLE,
            10.0,
            GameplayEffect.CROWD_BOOST
    ),

    CROWD_MULTIPLIER(
            "Crowd Multiplier",
            "Improves crowd energy gain.",
            Rarity.COMMON,
            null,
            ItemType.EQUIPPED,
            1.05,
            GameplayEffect.CROWD_MULTIPLIER
    ),

    CROWD_BOOST_IF_STAMINA_BELOW_20_AFTER_EVENT_4(
            "Crowd Boost If Stamina Below 20 After Event 4",
            "Conditional crowd boost.",
            Rarity.COMMON,
            null,
            ItemType.CONDITIONAL,
            15.0,
            GameplayEffect.CROWD_BOOST_IF_STAMINA_BELOW_20_AFTER_EVENT_4
    ),

    CROWD_BOOST_PER_EVENT_IF_LINEUP_FULL(
            "Crowd Boost Per Event If Lineup Full",
            "Conditional crowd boost when the lineup is full.",
            Rarity.COMMON,
            null,
            ItemType.CONDITIONAL,
            5.0,
            GameplayEffect.CROWD_BOOST_PER_EVENT_IF_LINEUP_FULL
    ),

    CROWD_AND_STAMINA_BOOST_IF_FIRST_EVENT_WIN(
            "Crowd And Stamina Boost If First Event Win",
            "Conditional crowd and stamina boost.",
            Rarity.COMMON,
            null,
            ItemType.CONDITIONAL,
            25.0,
            GameplayEffect.CROWD_AND_STAMINA_BOOST_IF_FIRST_EVENT_WIN
    ),

    CROWD_2X_IF_THREE_WIN_STREAK(
            "Crowd 2x If Three Win Streak",
            "Conditional crowd multiplier.",
            Rarity.RARE,
            null,
            ItemType.CONDITIONAL,
            2.0,
            GameplayEffect.CROWD_2X_IF_THREE_WIN_STREAK
    ),

    STAMINA_RECOVER_IF_BELOW_25(
            "Stamina Recover If Below 25",
            "Recovers stamina when it drops too low.",
            Rarity.RARE,
            StatType.STAMINA,
            ItemType.CONDITIONAL,
            30.0,
            GameplayEffect.STAMINA_RECOVER_IF_BELOW_25
    ),

    AUTO_WIN_IF_CROWD_BELOW_20_AFTER_EVENT_3(
            "Auto Win If Crowd Below 20 After Event 3",
            "Auto-wins the next event if the crowd is struggling.",
            Rarity.RARE,
            null,
            ItemType.CONDITIONAL,
            1.0,
            GameplayEffect.AUTO_WIN_IF_CROWD_BELOW_20_AFTER_EVENT_3
    ),

    STAMINA_RECOVER_ALL_IF_LOWEST_BELOW_35(
            "Stamina Recover All If Lowest Below 35",
            "Restores stamina to the full lineup when one artist is struggling.",
            Rarity.VERY_RARE,
            null,
            ItemType.CONDITIONAL,
            25.0,
            GameplayEffect.STAMINA_RECOVER_ALL_IF_LOWEST_BELOW_35
    ),

    TICKET_SALES_2X_IF_CROWD_ABOVE_70_BEFORE_FINAL(
            "Ticket Sales 2x If Crowd Above 70 Before Final",
            "Doubles ticket sales late in a strong concert.",
            Rarity.VERY_RARE,
            null,
            ItemType.CONDITIONAL,
            2.0,
            GameplayEffect.TICKET_SALES_2X_IF_CROWD_ABOVE_70_BEFORE_FINAL
    ),

    CROWD_BOOST_ON_FINAL_EVENT(
            "Crowd Boost On Final Event",
            "Boosts crowd energy during the final event.",
            Rarity.VERY_RARE,
            null,
            ItemType.CONDITIONAL,
            50.0,
            GameplayEffect.CROWD_BOOST_ON_FINAL_EVENT
    ),

    FULL_ROSTER_INCOME_2X(
            "Full Roster Income 2x",
            "Doubles income when every artist is fully equipped.",
            Rarity.ULTRA,
            null,
            ItemType.CONDITIONAL,
            2.0,
            GameplayEffect.FULL_ROSTER_INCOME_2X
    ),

    FINAL_WIN_INCOME_2X(
            "Final Win Income 2x",
            "Doubles income after winning the final event.",
            Rarity.MYTHIC,
            null,
            ItemType.CONDITIONAL,
            2.0,
            GameplayEffect.FINAL_WIN_INCOME_2X
    );

    private final String name;
    private final String description;
    private final Rarity rarity;
    private final StatType targetStat;
    private final ItemType itemType;
    private final double defaultValue;
    private final GameplayEffect gameplayEffect;

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

    public String toString()
    {
        return name;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Rarity getRarity() { return rarity; }
    public StatType getTargetStat() { return targetStat; }
    public ItemType getItemType() { return itemType; }
    public double getDefaultValue() { return defaultValue; }
    public GameplayEffect getGameplayEffect() { return gameplayEffect; }
    public StatModifier getStatModifier() { return gameplayEffect.createStatModifier(defaultValue); }
    public ConcertModifier getConcertModifier() { return gameplayEffect.createConcertModifier(defaultValue); }
}
