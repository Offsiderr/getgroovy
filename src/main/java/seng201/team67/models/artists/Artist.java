package seng201.team67.models.artists;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import seng201.team67.interfaces.Purchasable;
import seng201.team67.models.Skill;
import seng201.team67.models.enums.GameplayEffect;
import seng201.team67.models.enums.ItemEffects;
import seng201.team67.models.enums.items.StatType;
import seng201.team67.models.items.ConditionalItem;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;

import java.util.ArrayList;


//Allows Jason to recognise the sub-class types.
/**
 * Represents a hireable artist in the game. It stores the shared artist stats, items, and skill state used by artist subclasses.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Popstar.class, name = "POP"),
        @JsonSubTypes.Type(value = Rapper.class, name = "RAP"),
        @JsonSubTypes.Type(value = Rockstar.class, name = "ROCK")
})

public abstract class Artist implements Purchasable {

    /** Constant that defines the min star power. */
    private static final int MIN_STAR_POWER = 1;
    /** Constant that defines the max star power. */
    private static final int MAX_STAR_POWER = 5;

    /** Text value for the name. */
    private String name;
    /** Text value for the description. */
    private String description;

    /** Numeric value for the retirement chance. */
    private int retirementChance;
    /** Numeric value for the consecutive tours without break. */
    private int consecutiveToursWithoutBreak;
    /** Numeric value for tolerance (called health internally). */
    private int health;
    /** Numeric value for the base stamina. this is the artists base stamina, not to be confused with their actual stamina */
    private int baseStamina;
    /** Numeric value for the stamina. */
    private int stamina;
    /** Numeric value for star power. Perception is star power in our game. */
    private int starPower;
    /** Numeric value for the skill level. */
    private int skillLevel = 1;
    /** Numeric value for the base pay. */
    private static final double basePay         = 20; //Unfortunately these cannot be included in the game config;
    /** Numeric value for the base hiring cost. */
    private static final double baseHiringCost = 110;//as they are imported through JSON with Jackson.
    /** Whether owned. */
    public boolean owned = false;

    /** Collection that stores the items. */
    private ArrayList<Item> items = new ArrayList<>();
    /** The skill. */
    private Skill skill;

    /**
     * Creates a new artist.
     * It initializes the state needed for the surrounding game flow.
     * @param name the name value to use
     * @param starPower the star power value to apply
     * @param stamina the stamina value to apply
     * @param health the health value to apply
     * @param description the description text to use
     */
    public Artist(String name, int starPower, int stamina, int health, String description)
    {
        this.name = name;
        this.description = description;
        this.health = health;
        this.stamina = stamina;
        this.starPower = clampBaseStarPower(starPower);
        this.skillLevel = Math.max(MIN_STAR_POWER, this.starPower);
        this.baseStamina = stamina;
    }

    //Getters

    /**
     * Returns the pay.
     * @return The pay.
     */
    public double getPay()
    {
        return basePay * getStarPower();
    }

    /**
     * Returns the cost.
     * @return The cost.
     */
    public double getCost()
    {
        return baseHiringCost * getStarPower();
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
     * Returns the health.
     * @return The health.
     */
    public int getHealth()
    {
        return getModifiedHealth();
    }

    /**
     * Returns the stamina.
     * @return The stamina.
     */
    public int getStamina()
    {
        return getModifiedStamina();
    }

    /**
     * Returns the base stamina.
     * @return The base stamina.
     */
    public int getBaseStamina()
    {
        return baseStamina;
    }

    /**
     * Returns the base star power value.
     * @return The base star power value.
     */
    public int getBaseStarPowerValue()
    {
        return starPower;
    }

    /**
     * Returns the current stamina value.
     * @return The current stamina value.
     */
    public int getCurrentStaminaValue()
    {
        return stamina;
    }

    /**
     * Returns the base health value.
     * @return The base health value.
     */
    public int getBaseHealthValue()
    {
        return health;
    }

    /**
     * Returns the star power.
     * @return The star power.
     */
    public int getStarPower()
    {
        return getModifiedStarPower();
    }

    /**
     * Returns the description.
     * @return The description.
     */
    public String getDescription(){return description;}

    /**
     * Returns the tolerance. Heath was the original name, so it appears on the artist class
     * as such.
     * @return
     */
    public int getTolerance()
    {
        return getModifiedTolerance();
    }

    /**
     * Returns the type.
     * @return The type.
     */
    public String getType(){return "Artist";}

    /**
     * Returns the skill level.
     * @return The skill level.
     */
    public Integer getSkillLevel(){return skillLevel;}

    /**
     * Returns the retirement chance.
     * @return The retirement chance.
     */
    public int getRetirementChance()
    {
        return retirementChance;
    }

    /**
     * Returns the consecutive tours without break.
     * @return The consecutive tours without break.
     */
    public int getConsecutiveToursWithoutBreak()
    {
        return consecutiveToursWithoutBreak;
    }

    /**
     * Returns the image path.
     * @return The image path.
     */
    public String getImagePath()
    {
        return "/images/Artists/" + this.name + ".png";
    }

    /**
     * Returns the items.
     * @return The items.
     */
    public ArrayList<Item> getItems()
    {
        return new ArrayList<>(items);
    }


    /**
     * Returns the skill.
     * @return The skill.
     */
    public Skill getSkill()
    {
        return skill;
    }


    /**
     * Returns whether skill.
     * @return True if skill, otherwise false.
     */
    public boolean hasSkill()
    {
        return skill != null;
    }

    //Setters

    /**
     * Sets the base stamina.
     * @param stamina the stamina value to apply
     */
    public void setBaseStamina(int stamina)
    {
        this.baseStamina = stamina;
    }

    /**
     * Sets the skill.
     * It normalizes the stored value to keep the state consistent.
     * @param skill the skill to associate with the artist
     */
    public void setSkill(Skill skill)
    {
        removeFlatSkillBonuses(this.skill);
        this.skill = skill;
        applyFlatSkillBonuses(skill);
    }

    /**
     * Increases the skill level.
     * It updates related state as needed while performing the operation.
     */
    public void increaseSkillLevel()
    {
        removeFlatSkillBonuses(this.skill);
        skillLevel += 1;
        applyFlatSkillBonuses(this.skill);
    }

    /**
     * Adjusts the skill level.
     * It updates related state as needed while performing the operation.
     * @param amount the amount to apply
     */
    public void changeSkillLevel(int amount)
    {
        removeFlatSkillBonuses(this.skill);
        skillLevel = Math.max(1, skillLevel + amount);
        applyFlatSkillBonuses(this.skill);
    }

    /**
     * Adjusts the star power.
     * @param amount the amount to apply
     */
    public void changeStarPower(int amount)
    {
        starPower = clampBaseStarPower(starPower + amount);
    }

    /**
     * Sets the tolerance.
     * @param health the health value to apply
     */
    public void setTolerance(int health)
    {
        this.health = health;
    }

    /**
     * Sets the stamina.
     * @param stamina the stamina value to apply
     */
    public void setStamina(int stamina)
    {
        this.stamina = stamina;
        if(this.stamina > baseStamina)
        {
            this.stamina = baseStamina;
        }

        if(this.stamina < 0)
        {
            this.stamina = 0;
        }
    }

    /**
     * Resets the stamina.
     */
    public void resetStamina()
    {
        this.stamina = baseStamina;
    }

    /**
     * Increases the retirement chance.
     * @param amount the amount to apply
     */
    public void increaseRetirementChance(int amount)
    {
        retirementChance = Math.max(0, retirementChance + amount);
    }

    /**
     * Increments the consecutive tours without break.
     */
    public void incrementConsecutiveToursWithoutBreak()
    {
        consecutiveToursWithoutBreak += 1;
    }

    /**
     * Resets the consecutive tours without break.
     */
    public void resetConsecutiveToursWithoutBreak()
    {
        consecutiveToursWithoutBreak = 0;
    }

    /**
     * Processes the effects to apply.
     * @return A list containing the effects to apply.
     */
    public ArrayList<ItemEffects> effectsToApply()
    {
        ArrayList<ItemEffects> allItemEffects = new ArrayList<>();
        for (Item item : items)
        {
            for (ItemEffects itemEffects : item.getEffects())
            {
                allItemEffects.add(itemEffects);
            }
        }
        return allItemEffects;
    }

    /**
     * Adds the item.
     * @param item the item involved in the operation
     */
    public void addItem(Item item)
    {
        items.add(item);
    }

    /**
     * Removes the item.
     * @param item the item involved in the operation
     */
    public void removeItem(Item item)
    {
        items.remove(item);
    }

    //Returns true if anything actually changed (so therefore true means yes trigger the UI event)
    /**
     * Calculates the effect.
     * It uses the current game state and supplied context when producing the result.
     * @param itemEffects the item effects
     * @return True if effect, otherwise false.
     */
    public Boolean calculateEffect(ItemEffects itemEffects)
    {
        return calculateEffect(null, itemEffects);
    }

    /**
     * Calculates the effect.
     * It uses the current game state and supplied context when producing the result.
     * @param item the item involved in the operation
     * @param itemEffects the item effects
     * @return True if effect, otherwise false.
     */
    public Boolean calculateEffect(Item item, ItemEffects itemEffects)
    {
        int value = getEffectValue(item, itemEffects);
        if (value == 0 || itemEffects.getTargetStat() == null) return false;

        switch (itemEffects.getTargetStat()) {
            case STAR_POWER -> starPower = clampBaseStarPower(starPower + value);
            case STAMINA    -> setStamina(stamina + value);
            case TOLERANCE -> health += value;
        }
        return true;
    }

    /**
     * Returns the effect value.
     * @param itemEffects the item effects
     * @return The effect value.
     */
    public int getEffectValue(ItemEffects itemEffects)
    {
        return getEffectValue(null, itemEffects);
    }

    /**
     * Returns the effect value.
     * @param item the item involved in the operation
     * @param itemEffects the item effects
     * @return The effect value.
     */
    public int getEffectValue(Item item, ItemEffects itemEffects)
    {
        return itemEffects.getGameplayEffect()
                .createStatModifier(resolveEffectValue(item, itemEffects))
                .apply(this, resolveEffectValue(item, itemEffects));
    }

    /**
     * Returns the modified star power.
     * @return The modified star power.
     */
    public int getModifiedStarPower()
    {
        return starPower + getEquipableEffectValue(StatType.STAR_POWER) + getSkillEffectValue(StatType.STAR_POWER);
    }

    /**
     * Returns the modified stamina.
     * @return The modified stamina.
     */
    public int getModifiedStamina()
    {
        return stamina + getEquipableEffectValue(StatType.STAMINA);
    }

    /**
     * Returns the modified health.
     * @return The modified health.
     */
    public int getModifiedHealth()
    {
        return getModifiedTolerance();
    }

    private int getModifiedTolerance()
    {
        int tolerance = health;
        if (hasSkill() && skill.hasEffect(GameplayEffect.RETIREMENT_RISK))
        {
            tolerance = Math.max(0, (int) Math.round(tolerance * 0.6));
        }
        return tolerance + getEquipableEffectValue(StatType.HEALTH);
    }

    private int getEquipableEffectValue(StatType statType)
    {
        int value = 0;

        for (Item item : items)
        {
            if (!(item instanceof EquippedItem) || item instanceof ConditionalItem)
            {
                continue;
            }

            for (ItemEffects itemEffects : item.getEffects())
            {
                if (itemEffects.getTargetStat() == statType)
                {
                    value += getEffectValue(item, itemEffects);
                }
            }
        }

        return value;
    }

    private int getSkillEffectValue(StatType statType)
    {
        if (!hasSkill() || !skill.hasStatModifier())
        {
            return 0;
        }

        return switch (statType) {
            case STAR_POWER -> skill.hasEffect(GameplayEffect.FLAT_STAR_POWER_BOOST) && skill.getMultiplier() > 1.0
                    ? GameplayEffect.FLAT_STAR_POWER_BOOST.createStatModifier(skill.getMultiplier()).apply(this, 0)
                    : 0;
            case STAMINA, HEALTH, TOLERANCE -> 0;
        };
    }

    private void applyFlatSkillBonuses(Skill skill)
    {
        if (skill == null || !skill.hasStatModifier())
        {
            return;
        }

        if (skill.hasEffect(GameplayEffect.FLAT_STAMINA_BOOST))
        {
            int staminaBoost = GameplayEffect.FLAT_STAMINA_BOOST.createStatModifier(skill.getMultiplier()).apply(this, 0);
            baseStamina += staminaBoost;
            stamina += staminaBoost;
        }
    }

    private void removeFlatSkillBonuses(Skill skill)
    {
        if (skill == null || !skill.hasStatModifier())
        {
            return;
        }

        if (skill.hasEffect(GameplayEffect.FLAT_STAMINA_BOOST))
        {
            int staminaBoost = GameplayEffect.FLAT_STAMINA_BOOST.createStatModifier(skill.getMultiplier()).apply(this, 0);
            baseStamina = Math.max(0, baseStamina - staminaBoost);
            stamina = Math.min(stamina, baseStamina);
        }
    }

    private double resolveEffectValue(Item item, ItemEffects itemEffects)
    {
        if (item != null && item.getMultiplier() != null)
        {
            return item.getMultiplier();
        }
        return itemEffects.getDefaultValue();
    }

    private int clampBaseStarPower(int value)
    {
        return Math.max(MIN_STAR_POWER, Math.min(MAX_STAR_POWER, value));
    }
}
