package seng201.team67.unittests.services;

import javafx.scene.layout.HBox;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.models.enums.Rarity;
import seng201.team67.services.GachaService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GachaServiceTest {

    @Test
    void getPickedArtistsReturnsOneArtistPerSlot() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        List<Artist> pool = List.of(
                new Popstar("One", 1, "Pop"),
                new Rapper("Two", 2, "Rap"),
                new Rockstar("Three", 3, "Rock"),
                new Popstar("Four", 1, "Pop")
        );
        gameEnvironment.setArtistPool(pool);

        List<Artist> picked = new GachaService(gameEnvironment).getPickedArtists(
                List.of(new HBox(), new HBox(), new HBox()),
                Rarity.COMMON
        );

        assertEquals(3, picked.size());
        assertTrue(picked.stream().allMatch(pool::contains));
    }
}
