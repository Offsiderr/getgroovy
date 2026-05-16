package seng201.team67.models;

import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.enums.GameplayEffect;
import seng201.team67.models.enums.Rarity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a skill that can modify an artist's stats or payout behaviour.
 */
public class Skill {

    private String id;
    private String name;
    private String description;
    private String artistType;
    private double multiplier;
    private List<GameplayEffect> effects;
    private StatModifier statModifier;
    private PayoutModifier payoutModifier;

    public Skill(String id, String name, String description, String artistType,
                 double multiplier, List<GameplayEffect> effects, StatModifier statModifier, PayoutModifier payoutModifier)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.artistType = artistType;
        this.multiplier = multiplier;
        this.effects = effects == null ? new ArrayList<>() : new ArrayList<>(effects);
        this.statModifier = statModifier;
        this.payoutModifier = payoutModifier;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getArtistType()
    {
        return artistType;
    }

    public double getMultiplier()
    {
        return multiplier;
    }

    public List<GameplayEffect> getEffects()
    {
        return new ArrayList<>(effects);
    }

    public boolean hasEffect(GameplayEffect effect)
    {
        return effects.contains(effect);
    }

    public StatModifier getStatModifier()
    {
        return statModifier;
    }


    public PayoutModifier getPayoutModifier()
    {
        return payoutModifier;
    }

    public boolean hasStatModifier()
    {
        return statModifier != null;
    }


    public boolean hasPayoutModifier()
    {
        return payoutModifier != null;
    }
}
