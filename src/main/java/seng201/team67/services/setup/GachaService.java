package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.items.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides gacha operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class GachaService {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;

    /**
     * Creates a new gacha service.
     * @param gameEnvironment the active game environment
     */
    public GachaService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Returns the picked artists.
     * It derives the value from the current state before returning it.
     * @param slotCount the numeric value for the slot count
     * @param rarity the rarity
     * @return The picked artists.
     */
    public List<Artist> getPickedArtists(int slotCount, Rarity rarity)
    {
        List<Artist> pool = gameEnvironment.getArtistPool();
        Collections.shuffle(pool);

        List<Artist> picked = new ArrayList<>();
        for (int i = 0; i < slotCount; i++)
        {
            int selectedStarpower = rarity.get_starpower();
            int z = i;
            while (z < pool.size() && (pool.get(z).owned || pool.get(z).getStarPower() != selectedStarpower))
            {
                z += 1;
            }

            if (z < pool.size())
            {
                picked.add(pool.get(z));
            }
        }

        return picked;
    }

    /**
     * Returns the picked items.
     * It derives the value from the current state before returning it.
     * @param slotCount the numeric value for the slot count
     * @param rarity the rarity
     * @return The picked items.
     */
    public List<Item> getPickedItems(int slotCount, Rarity rarity)
    {
        List<Item> pool = new ArrayList<>(gameEnvironment.getAllItems());
        Collections.shuffle(pool);

        List<Item> picked = new ArrayList<>();
        for (int i = 0; i < slotCount; i++)
        {
            int z = i;
            while (z < pool.size() && (picked.contains(pool.get(z)) || pool.get(z).getRarity().getIndex() > rarity.getIndex()))
            {
                z += 1;
            }

            if (z < pool.size())
            {
                picked.add(pool.get(z));
            }
        }

        return picked;
    }
}
