package seng201.team67.models;

import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.enums.Rarity;

/**
 * Represents a skill that can modify an artist's stats or payout behaviour.
 */
public class Skill {

    private String id;
    private String name;
    private String description;
    private String artistType;
    private Rarity rarity;
    private StatModifier statModifier;
    private PayoutModifier payoutModifier;

    public Skill(String id, String name, String description, String artistType, Rarity rarity,
                 StatModifier statModifier, PayoutModifier payoutModifier)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.artistType = artistType;
        this.rarity = rarity;
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
