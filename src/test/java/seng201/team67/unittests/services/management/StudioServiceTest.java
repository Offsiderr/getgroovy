package seng201.team67.unittests.services.management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.services.management.StudioService;
import seng201.team67.services.setup.DifficultyService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudioServiceTest {

    private GameEnvironment gameEnvironment;
    private StudioService studioService;
    private Artist ownedArtist;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        ownedArtist = new Popstar("Owned", 1, "Pop");
        ownedArtist.owned = true;
        gameEnvironment.setArtistPool(List.of(
                ownedArtist,
                new Popstar("Common", 1, "Pop"),
                new Rapper("Rare", 2, "Rap"),
                new Rockstar("Very Rare", 3, "Rock")
        ));
        studioService = new StudioService(gameEnvironment);
    }

    @Test
    @DisplayName("Studio artist pool contains only unowned artists")
    void artistPoolContainsOnlyUnownedArtists() {
        ArrayList<Artist> pool = studioService.getArtistPurchasePool();

        assertFalse(pool.contains(ownedArtist));
        assertFalse(pool.isEmpty());
        assertTrue(pool.stream().noneMatch(artist -> artist.owned));
        assertTrue(gameEnvironment.getArtistPool().containsAll(pool));
    }

    @Test
    @DisplayName("Studio artist pool is cached after generation")
    void artistPoolIsCachedAfterGeneration() {
        ArrayList<Artist> firstPool = studioService.getArtistPurchasePool();
        ArrayList<Artist> secondPool = studioService.getArtistPurchasePool();

        assertSame(gameEnvironment.getArtistPurchasePool(), firstPool);
        assertEquals(firstPool, secondPool);
        assertTrue(gameEnvironment.isArtistPoolGenerated());
    }
}
