package seng201.team67.models.artists;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import seng201.team67.interfaces.Purchasable;
import seng201.team67.models.Skill;
import seng201.team67.models.enums.SkillEffects;
import seng201.team67.models.enums.items.Effect;
import seng201.team67.models.enums.items.StatType;
import seng201.team67.models.items.ConditionalItem;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;

import java.util.ArrayList;


//Allows Jason to recognise the sub-class types.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Popstar.class, name = "POP"),
        @JsonSubTypes.Type(value = Rapper.class, name = "RAP"),
        @JsonSubTypes.Type(value = Rockstar.class, name = "ROCK")
})

public abstract class Artist implements Purchasable {


    private String name;
    private String description;

    private int health;
    private int baseStamina;
    private int stamina;
    private int starPower;
    private int skillLevel = 1;
    private static final double basePay         = 5; //Unfortunately these cannot be included in the game config;
    private static final double baseHiringCost = 7;//as they are imported through JSON with Jackson.
    public boolean owned = false;

    private ArrayList<Item> items = new ArrayList<>();
    private Skill skill;

    public Artist(String name, int starPower, int stamina, int health, String description)
    {
        this.name = name;
        this.description = description;
        this.health = health;
        this.stamina = stamina;
        this.starPower = starPower;
        this.baseStamina = stamina;
    }

    //Getters

    public double getPay()
    {
        return basePay * getStarPower();
    }

    public double getCost()
    {
        return baseHiringCost * getStarPower();
    }

    public String getName()
    {
        return name;
    }

    public int getHealth()
    {
        return getModifiedHealth();
    }

    public int getStamina()
    {
        return getModifiedStamina();
    }

    public int getBaseStamina()
    {
        return baseStamina;
    }

    public int getBaseStarPowerValue()
    {
        return starPower;
    }

    public int getCurrentStaminaValue()
    {
        return stamina;
    }

    public int getBaseHealthValue()
    {
        return health;
    }

    public int getStarPower()
    {
        return getModifiedStarPower();
    }

    public String getDescription(){return description;}

    public String getType(){return "Artist";}

    public Integer getSkillLevel(){return skillLevel;}

    public String getImagePath()
    {
        return "/images/Artists/" + this.name + ".png";
    }

    public ArrayList<Item> getItems()
    {
        return new ArrayList<>(items);
    }


    public Skill getSkill()
    {
        return skill;
    }


    public boolean hasSkill()
    {
        return skill != null;
    }

    //Setters


    public void setSkill(Skill skill)
    {
        this.skill = skill;
        applyFlatSkillBonuses(skill);
    }

    public void increaseSkillLevel()
    {
        skillLevel += 1;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

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

    public void resetStamina()
    {
        this.stamina = baseStamina;
    }

    public ArrayList<Effect> effectsToApply()
    {
        ArrayList<Effect> allEffects = new ArrayList<>();
        for (Item item : items)
        {
            for (Effect effect : item.getEffects())
            {
                allEffects.add(effect);
            }
        }
        return allEffects;
    }

    public void addItem(Item item)
    {
        items.add(item);
    }

    public void removeItem(Item item)
    {
        items.remove(item);
    }

    //Returns true if anything actually changed (so therefore true means yes trigger the UI event)
    public Boolean calculateEffect(Effect effect)
    {
        return calculateEffect(null, effect);
    }

    public Boolean calculateEffect(Item item, Effect effect)
    {
        int value = getEffectValue(item, effect);
        if (value == 0 || effect.getTargetStat() == null) return false;

        switch (effect.getTargetStat()) {
            case STAR_POWER -> starPower += value;
            case STAMINA    -> setStamina(stamina + value);
            case HEALTH     -> health += value;
        }
        return true;
    }

    public int getEffectValue(Effect effect)
    {
        return getEffectValue(null, effect);
    }

    public int getEffectValue(Item item, Effect effect)
    {
        return effect.getModifier().apply(this, resolveEffectValue(item, effect));
    }

    public int getModifiedStarPower()
    {
        return starPower + getEquipableEffectValue(StatType.STAR_POWER) + getSkillEffectValue(StatType.STAR_POWER);
    }

    public int getModifiedStamina()
    {
        return stamina + getEquipableEffectValue(StatType.STAMINA);
    }

    public int getModifiedHealth()
    {
        return health + getEquipableEffectValue(StatType.HEALTH);
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

            for (Effect effect : item.getEffects())
            {
                if (effect.getTargetStat() == statType)
                {
                    value += getEffectValue(item, effect);
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
            case STAR_POWER -> skill.hasEffect(SkillEffects.FLAT_STAR_POWER_BOOST)
                    ? skill.getStatModifier().apply(this, 0)
                    : 0;
            case STAMINA, HEALTH -> 0;
        };
    }

    private void applyFlatSkillBonuses(Skill skill)
    {
        if (skill == null || !skill.hasStatModifier())
        {
            return;
        }

        if (skill.hasEffect(SkillEffects.FLAT_STAMINA_BOOST))
        {
            int staminaBoost = skill.getStatModifier().apply(this, 0);
            baseStamina += staminaBoost;
            stamina += staminaBoost;
        }
    }

    private double resolveEffectValue(Item item, Effect effect)
    {
        if (item != null && item.getMultiplier() != null)
        {
            return item.getMultiplier();
        }
        return effect.getDefaultValue();
    }
}
