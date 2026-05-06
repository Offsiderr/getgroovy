package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.services.ArtistSelectionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArtistSelectionServiceTest {

    @Test
    void pickArtistsReturnsEligibleUniqueArtists() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        gameEnvironment.setArtistPool(List.of(
                new Popstar("One", 1, "Pop"),
                new Rapper("Two", 2, "Rap"),
                new Rockstar("Three", 3, "Rock"),
                new Popstar("Four", 1, "Pop"),
                new Rapper("Five", 2, "Rap"),
                new Rockstar("Six", 3, "Rock"),
                new Popstar("Seven", 4, "Too strong")
        ));

        List<Artist> picked = new ArtistSelectionService(gameEnvironment).pickArtists();

        assertEquals(gameEnvironment.getConfig().startingArtistPoolSize, picked.size());
        assertEquals(picked.size(), picked.stream().distinct().count());
        assertTrue(picked.stream().allMatch(artist -> artist.getStarPower() <= gameEnvironment.getConfig().maxSPInStartingSelection));
    }

    private GameEnvironment createConfiguredEnvironment() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        gameEnvironment.setDifficulty(0);
        return gameEnvironment;
    }
}
