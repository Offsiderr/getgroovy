package seng201.team67.services.management;

import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MarketService {

    private final GameEnvironment gameEnvironment;
    private final Random random = new Random();

    public MarketService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

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
                if (!item.getOwned() && item.getRarity() == rarity)
                {
                    candidates.add(item);
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
