package seng201.team67.services.management;

import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.Rarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class StudioService {

    private final GameEnvironment gameEnvironment;
    private final Random random = new Random();

    public StudioService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    public ArrayList<Artist> getArtistPurchasePool()
    {
        if (gameEnvironment.isArtistPoolGenerated())
        {
            return gameEnvironment.getArtistPurchasePool();
        }

        ArrayList<Artist> purchasePool = new ArrayList<>();
        ArrayList<Rarity> rarities = new ArrayList<>(Arrays.asList(Rarity.COMMON, Rarity.RARE, Rarity.VERY_RARE));
        Collections.shuffle(rarities);

        for (Rarity rarity : rarities)
        {
            ArrayList<Artist> candidates = new ArrayList<>();
            for (Artist artist : gameEnvironment.getArtistPool())
            {
                if (!artist.owned && artist.getStarPower() == rarity.get_starpower())
                {
                    candidates.add(artist);
                }
            }

            if (!candidates.isEmpty())
            {
                purchasePool.add(candidates.get(random.nextInt(candidates.size())));
            }
        }

        gameEnvironment.setArtistPurchasePool(purchasePool);
        gameEnvironment.setArtistPoolGenerated(true);
        return gameEnvironment.getArtistPurchasePool();
    }
}
