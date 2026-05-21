package seng201.team67.models.enums;

/**
 * Represents the available rarities used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum Rarity {

    /** The common. */
    COMMON("Common", 1, 2, 0.2, 1),
    /** The rare. */
    RARE("Rare", 1, 3, 0.15, 2),
    /** The very rare. */
    VERY_RARE("Very Rare", 2, 4, 0.30, 3),
    /** The ultra. */
    ULTRA("Ultra", 3, 5, 0.3, 4), //this is the maximum purchaseable gatcha.
    /** The mythic. */
    MYTHIC("Mythic", 4, 5, 0.75, 5);


    /** Text value for the display name. */
    private final String displayName;
    /** Numeric value for the star power min. */
    private final int starPowerMin;
    /** Numeric value for the star power max. */
    private final int starPowerMax;
    /** Numeric value for the max chance. */
    private final double maxChance; //The chance of getting an artist/item from a gatcha with the maximum star power allowed
    /** Numeric value for the index. */
    private final int index; //This helps with the item gatchas, so that it can check if the rarity that is pulled is less than the current rarity

    //Expand this to allow items later

    /**
     * Creates a new rarity.
     * @param displayName the text value for the display name
     * @param starPowerMin the numeric value for the star power min
     * @param starPowerMax the numeric value for the star power max
     * @param maxChance the numeric value for the max chance
     * @param index the numeric value for the index
     */
    Rarity(String displayName, int starPowerMin, int starPowerMax, double maxChance, int index) {
        this.displayName = displayName;
        this.starPowerMin = starPowerMin;
        this.starPowerMax = starPowerMax;
        this.maxChance = maxChance;
        this.index = index;
    }

    /**
     * Returns the index.
     * @return The index.
     */
    public int getIndex()
    {
        return index;
    }


    //mediocre code, will redo this later
    /**
     * Returns the starpower.
     * It derives the value from the current state before returning it.
     * @return The starpower.
     */
    public int get_starpower()
    {
        int range = starPowerMax - starPowerMin + 1;
        double[] weights = new double[range];
        double totatWeight = 0;

        //get our weights
        for (int i = 0; i < range; i++)
        {
            weights[i] = 1.0 - (1.0 - maxChance) * ((double) i / (range - 1));
            totatWeight += weights[i];
        }

        double roll = Math.random() * totatWeight;
        double cumulative = 0;

        for (int i = 0; i < range; i++)
        {
            cumulative += weights[i];
            if (roll < cumulative)
            {
                return starPowerMin + i;
            }
        }

        //fallback
        return starPowerMax;
    }
}
