package seng201.team67.unittests;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.services.management.StudioService;

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

        StudioService studioService = new StudioService(gameEnvironment);
        List<Artist> purchasePool = studioService.getArtistPurchasePool();
        int originalSize = purchasePool.size();
        Artist removedArtist = purchasePool.getFirst();
        long originalOccurrences = purchasePool.stream()
                .filter(artist -> artist.equals(removedArtist))
                .count();

        gameEnvironment.removeArtistFromPurchasePool(removedArtist);

        List<Artist> updatedPool = studioService.getArtistPurchasePool();
        long updatedOccurrences = updatedPool.stream()
                .filter(artist -> artist.equals(removedArtist))
                .count();

        assertEquals(originalSize - 1, updatedPool.size());
        assertEquals(originalOccurrences - 1, updatedOccurrences);
        assertFalse(updatedPool.isEmpty());
    }

    @Test
    void totalMoneyTrackingStartsAtZeroAndAccumulates() {
        GameEnvironment gameEnvironment = new GameEnvironment();

        assertEquals(0.0, gameEnvironment.getTotalMoneySpent(), 0.0001);
        assertEquals(0.0, gameEnvironment.getTotalMoneyEarnt(), 0.0001);

        gameEnvironment.addTotalMoneySpent(125.5);
        gameEnvironment.addTotalMoneyEarnt(300.25);

        assertEquals(125.5, gameEnvironment.getTotalMoneySpent(), 0.0001);
        assertEquals(300.25, gameEnvironment.getTotalMoneyEarnt(), 0.0001);
    }
}
