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
        List<Artist> pool = new ArrayList<>(gameEnvironment.getArtistPool());
        Collections.shuffle(pool);

        List<Artist> picked = new ArrayList<>();
        for (int i = 0; i < slotCount; i++)
        {
            int selectedStarpower = rarity.get_starpower();
            Artist artist = findArtistForStarPower(pool, picked, selectedStarpower);
            if (artist != null)
            {
                picked.add(artist);
            }
        }

        return picked;
    }

    private Artist findArtistForStarPower(List<Artist> pool, List<Artist> picked, int selectedStarpower)
    {
        for (Artist artist : pool)
        {
            if (!artist.owned && !picked.contains(artist) && artist.getStarPower() == selectedStarpower)
            {
                return artist;
            }
        }

        for (Artist artist : pool)
        {
            if (!artist.owned && !picked.contains(artist) && artist.getStarPower() < selectedStarpower)
            {
                return artist;
            }
        }

        return null;
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
