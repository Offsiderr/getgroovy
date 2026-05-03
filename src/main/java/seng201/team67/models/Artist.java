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

public abstract class Artist implements Purchasable {


    private String name;
    private String description;

    private int health;
    private int baseStamina;
    private int stamina;
    private int starPower;
    private static final double basePay         = 5; //Unfortunately these cannot be included in the game config;
    private static final double baseHiringCost = 7;//as they are imported through JSON with Jackson.
    public boolean owned = false;

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
        return basePay * starPower;
    }

    public double getCost()
    {
        return baseHiringCost * starPower;
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

    public int getStarPower()
    {
        return starPower;
    }

    public String getDescription(){return description;}

    public String getType(){return "Artist";}

    public String getImagePath()
    {
        return "/images/Artists/" + this.name + ".png";
    }

    //Setters

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



}
