package seng201.team67.services.management;

import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Provides market operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MarketService {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The random. */
    private final Random random = new Random();

    /**
     * Creates a new market service.
     * @param gameEnvironment the active game environment
     */
    public MarketService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Returns the item purchase pool.
     * @return The item purchase pool.
     */
    public ArrayList<Item> getItemPurchasePool()
    {
        if (gameEnvironment.isItemPoolGenerated())
        {
            return gameEnvironment.getItemPurchasePool();
        }

        ArrayList<Item> purchasePool = new ArrayList<>();
        ArrayList<Rarity> rarities = new ArrayList<>(Arrays.asList(Rarity.COMMON, Rarity.RARE, Rarity.COMMON));
        Collections.shuffle(rarities);

        for (Rarity rarity : rarities)
        {
            ArrayList<Item> candidates = new ArrayList<>();
            for (Item item : gameEnvironment.getAllItems())
            {
                if (!item.getOwned() && item.getRarity() == rarity && !purchasePool.contains(item))
                {
                    candidates.add(item);
                }
            }

            if (candidates.isEmpty())
            {
                for (Item item : gameEnvironment.getAllItems())
                {
                    if (!item.getOwned() && !purchasePool.contains(item))
                    {
                        candidates.add(item);
                    }
                }
            }

            if (!candidates.isEmpty())
            {
                purchasePool.add(candidates.get(random.nextInt(candidates.size())));
            }
        }

        gameEnvironment.setItemPurchasePool(purchasePool);
        gameEnvironment.setItemPoolGenerated(true);
        return gameEnvironment.getItemPurchasePool();
    }
}
