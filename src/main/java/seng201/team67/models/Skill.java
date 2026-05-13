package seng201.team67.models;

import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.SkillEffects;

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
    private Rarity rarity;
    private List<SkillEffects> effects;
    private StatModifier statModifier;
    private PayoutModifier payoutModifier;

    public Skill(String id, String name, String description, String artistType, Rarity rarity,
                 List<SkillEffects> effects, StatModifier statModifier, PayoutModifier payoutModifier)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.artistType = artistType;
        this.rarity = rarity;
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

    public List<SkillEffects> getEffects()
    {
        return new ArrayList<>(effects);
    }

    public boolean hasEffect(SkillEffects effect)
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
