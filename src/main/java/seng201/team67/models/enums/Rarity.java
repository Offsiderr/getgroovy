package seng201.team67.models.enums;

public enum Rarity {

    COMMON("Common", 1, 2, 0.2),
    RARE("Rare", 1, 3, 0.15),
    VERY_RARE("Very Rare", 2, 4, 0.30),
    ULTRA("Ultra", 3, 5, 0.3), //this is the maximum purchaseable gatcha.
    MYTHIC("Mythic", 4, 5, 0.75);


    private final String displayName;
    private final int starPowerMin;
    private final int starPowerMax;
    private final double maxChance; //The chance of getting an artist/item from a gatcha with the maximum star power allowed

    //Expand this to allow items later

    Rarity(String displayName, int starPowerMin, int starPowerMax, double maxChance) {
        this.displayName = displayName;
        this.starPowerMin = starPowerMin;
        this.starPowerMax = starPowerMax;
        this.maxChance = maxChance;
    }


    //mediocre code, will redo this later
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
