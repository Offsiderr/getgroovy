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
 * Represents skills used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Skill {

    /** Text value for the id. */
    private String id;
    /** Text value for the name. */
    private String name;
    /** Text value for the description. */
    private String description;
    /** Text value for the artist type. */
    private String artistType;
    /** The rarity. */
    private Rarity rarity;
    /** Numeric value for the multiplier. */
    private double multiplier;
    /** Collection that stores the effects. */
    private List<GameplayEffect> effects;
    /** The stat modifier. */
    private StatModifier statModifier;
    /** The payout modifier. */
    private PayoutModifier payoutModifier;

    /**
     * Creates a new skill.
     */
    public Skill()
    {
    }

    /**
     * Creates a new skill.
     * @param id the text value for the id
     * @param name the name value to use
     * @param description the description text to use
     * @param artistType the text value for the artist type
     * @param multiplier the multiplier used by the calculation
     * @param effects the list of effects
     * @param statModifier the stat modifier
     * @param payoutModifier the payout modifier
     */
    public Skill(String id, String name, String description, String artistType,
                 double multiplier, List<GameplayEffect> effects, StatModifier statModifier, PayoutModifier payoutModifier)
    {
        this(id, name, description, artistType, null, multiplier, effects, statModifier, payoutModifier);
    }

    /**
     * Creates a new skill.
     * It initializes the state needed for the surrounding game flow.
     * @param id the text value for the id
     * @param name the name value to use
     * @param description the description text to use
     * @param artistType the text value for the artist type
     * @param rarity the rarity
     * @param multiplier the multiplier used by the calculation
     * @param effects the list of effects
     * @param statModifier the stat modifier
     * @param payoutModifier the payout modifier
     */
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

    /**
     * Returns the id.
     * @return The id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Returns the name.
     * @return The name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the description.
     * @return The description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the artist type.
     * @return The artist type.
     */
    public String getArtistType()
    {
        return artistType;
    }

    /**
     * Returns the rarity.
     * @return The rarity.
     */
    public Rarity getRarity()
    {
        return rarity;
    }

    /**
     * Returns the multiplier.
     * @return The multiplier.
     */
    public double getMultiplier()
    {
        return multiplier;
    }

    /**
     * Returns the effects.
     * @return The effects.
     */
    public List<GameplayEffect> getEffects()
    {
        return new ArrayList<>(effects);
    }

    /**
     * Returns whether effect.
     * @param effect the effect
     * @return True if effect, otherwise false.
     */
    public boolean hasEffect(GameplayEffect effect)
    {
        return effects.contains(effect);
    }

    /**
     * Returns the stat modifier.
     * @return The stat modifier.
     */
    @JsonIgnore
    public StatModifier getStatModifier()
    {
        return statModifier;
    }

    /**
     * Returns the payout modifier.
     * @return The payout modifier.
     */
    @JsonIgnore
    public PayoutModifier getPayoutModifier()
    {
        return payoutModifier;
    }

    /**
     * Returns whether stat modifier.
     * @return True if stat modifier, otherwise false.
     */
    @JsonIgnore
    public boolean hasStatModifier()
    {
        return statModifier != null;
    }

    /**
     * Returns whether payout modifier.
     * @return True if payout modifier, otherwise false.
     */
    @JsonIgnore
    public boolean hasPayoutModifier()
    {
        return payoutModifier != null;
    }
}
