package seng201.team67.unittests;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameEnvironmentTest {

    @Test
    void removeArtistFromPurchasePoolLeavesRemainingStudioOptionsUntouched() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        gameEnvironment.setArtistPool(List.of(
                new Popstar("CommonOne", 1, "Pop"),
                new Popstar("CommonTwo", 1, "Pop"),
                new Rapper("RareOne", 2, "Rap"),
                new Rapper("RareTwo", 2, "Rap"),
                new Rockstar("VeryRareOne", 3, "Rock"),
                new Rockstar("VeryRareTwo", 3, "Rock")
        ));

        List<Artist> purchasePool = gameEnvironment.resetArtistPurchasePool();
        int originalSize = purchasePool.size();
        Artist removedArtist = purchasePool.getFirst();

        gameEnvironment.removeArtistFromPurchasePool(removedArtist);

        List<Artist> updatedPool = gameEnvironment.resetArtistPurchasePool();

        assertEquals(originalSize - 1, updatedPool.size());
        assertFalse(updatedPool.contains(removedArtist));
        assertTrue(updatedPool.stream().allMatch(artist -> !artist.equals(removedArtist)));
    }
}
