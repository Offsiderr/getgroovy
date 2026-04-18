package seng201.team67.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import seng201.team67.interfaces.Purchasable;

//Allows Jason to recognise the sub-class types.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Popstar.class, name = "POP"),
        @JsonSubTypes.Type(value = Rapper.class, name = "RAP"),
        @JsonSubTypes.Type(value = Rockstar.class, name = "ROCK")
})

public class Artist implements Purchasable {

    private String name;
    private String description;

    private int health;
    private int stamina;
    private int star_power; // 1 - Common, 2 - Rare 3 - Epic 4 - Legendary - 5 GOAT
    private static final double basePay         = 5;
    private static final double baseHiringCost = 7;

    public Artist(String name, int star_power, int stamina, int health, String description)
    {
        this.name = name;
        this.description = description;
        this.health = health;
        this.stamina = stamina;
        this.star_power = star_power;

    }

    //Getters

    public double getPay()
    {
        return basePay * star_power;
    }

    public double getCost()
    {
        return baseHiringCost * star_power;
    }

    public String getName()
    {
        return name;
    }

    public int getHealth()
    {
        return health;
    }

    public int getStamina()
    {
        return stamina;
    }

    public int getStar_power()
    {
        return star_power;
    }

    public String getDescription(){return description;}

    public String getType(){return "Artist";}

    //Setters

    public void setHealth(int health)
    {
        this.health = health;
    }

    public void setStamina(int stamina)
    {
        this.stamina = stamina;
    }

}
