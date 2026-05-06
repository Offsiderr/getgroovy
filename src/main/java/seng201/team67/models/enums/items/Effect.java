package seng201.team67.models.enums.items;

import seng201.team67.behaviours.EffectBehaviours;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.enums.Rarity;

public enum Effect {

    SECOND_WIND(
            "Second Wind",
            "Desperation fuels the performance.",
            Rarity.COMMON,
            StatType.STAR_POWER,
            ItemType.CONDITIONAL,
            EffectBehaviours.lowStaminaBoost(3, 30)
    ),

    STAR_FUELLED(
            "Star Fuelled",
            "Fame gives you energy.",
            Rarity.RARE,
            StatType.STAMINA,
            ItemType.CONDITIONAL,
            EffectBehaviours.highStarPowerStaminaBoost(10, 5)
    );

    private final String name;
    private final String description;
    private final Rarity rarity;
    private final StatType targetStat;
    private final ItemType itemType;
    private final StatModifier modifier;

    Effect(String name, String description, Rarity rarity,
           StatType targetStat, ItemType itemType, StatModifier modifier) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.targetStat = targetStat;
        this.itemType = itemType;
        this.modifier = modifier;
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
    public StatModifier getModifier() { return modifier; }
}