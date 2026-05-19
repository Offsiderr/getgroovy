package seng201.team67.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.enums.GameplayEffect;
import seng201.team67.models.enums.Rarity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a skill that can modify an artist's stats or payout behaviour.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Skill {

    private String id;
    private String name;
    private String description;
    private String artistType;
    private Rarity rarity;
    private double multiplier;
    private List<GameplayEffect> effects;
    private StatModifier statModifier;
    private PayoutModifier payoutModifier;

    public Skill()
    {
    }

    public Skill(String id, String name, String description, String artistType,
                 double multiplier, List<GameplayEffect> effects, StatModifier statModifier, PayoutModifier payoutModifier)
    {
        this(id, name, description, artistType, null, multiplier, effects, statModifier, payoutModifier);
    }

    public Skill(String id, String name, String description, String artistType,
                 Rarity rarity, double multiplier, List<GameplayEffect> effects,
                 StatModifier statModifier, PayoutModifier payoutModifier)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.artistType = artistType;
        this.rarity = rarity;
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

    public Rarity getRarity()
    {
        return rarity;
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

    @JsonIgnore
    public StatModifier getStatModifier()
    {
        return statModifier;
    }

    @JsonIgnore
    public PayoutModifier getPayoutModifier()
    {
        return payoutModifier;
    }

    @JsonIgnore
    public boolean hasStatModifier()
    {
        return statModifier != null;
    }

    @JsonIgnore
    public boolean hasPayoutModifier()
    {
        return payoutModifier != null;
    }
}
