package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LabelTest {

    @Test
    void constructorCopiesSelectedArtistsAndConfigValues() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        Artist artistOne = new Popstar("Ariana Grande", 3, "Pop artist");
        Artist artistTwo = new Rapper("Rod Wave", 2, "Rap artist");
        List<Artist> selectedArtists = List.of(artistOne, artistTwo);

        Label label = new Label("Moonlight Records", selectedArtists, gameEnvironment);

        assertEquals("Moonlight Records", label.getName());
        assertEquals(gameEnvironment.getConfig().startingCredits, label.getMoney());
        assertEquals(gameEnvironment.getConfig().artistsRosterLimit, label.getArtistsLimit());
        assertEquals(gameEnvironment.getConfig().lineUpLimit, label.getLineUpLimit());
        assertIterableEquals(selectedArtists, label.getAllArtists());
        assertIterableEquals(selectedArtists, label.getLineUp());
    }

    @Test
    void addArtistToAllAddsArtistWhenRosterHasCapacity() {
        Label label = new Label("Label", new ArrayList<>(), new GameEnvironment());
        Artist artist = new Popstar("Taylor Swift", 3, "Pop artist");

        boolean result = label.addArtistToAll(artist);

        assertTrue(result);
        assertTrue(label.getAllArtists().contains(artist));
    }

    @Test
    void addArtistToAllReturnsFalseWhenRosterIsFull() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        List<Artist> selectedArtists = new ArrayList<>();
        for (int i = 0; i < gameEnvironment.getConfig().artistsRosterLimit; i++) {
            selectedArtists.add(new Popstar("Artist" + i, 1, "Artist"));
        }
        Label label = new Label("Label", selectedArtists, gameEnvironment);

        boolean result = label.addArtistToAll(new Rapper("Late Artist", 2, "Rap artist"));

        assertFalse(result);
        assertEquals(gameEnvironment.getConfig().artistsRosterLimit, label.getAllArtists().size());
    }

    @Test
    void setLineUpReplacesCurrentLineupWhenWithinLimit() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        Artist artistOne = new Popstar("Artist One", 1, "Pop");
        Artist artistTwo = new Rapper("Artist Two", 2, "Rap");
        Artist artistThree = new Rockstar("Artist Three", 3, "Rock");
        Label label = new Label("Label", List.of(artistOne, artistTwo), gameEnvironment);

        label.setLineUp(List.of(artistThree));

        assertIterableEquals(List.of(artistThree), label.getLineUp());
    }

    @Test
    void setLineUpThrowsWhenLineupExceedsLimit() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        Label label = new Label("Label", new ArrayList<>(), gameEnvironment);
        List<Artist> oversizedLineup = List.of(
                new Popstar("One", 1, "Pop"),
                new Rapper("Two", 2, "Rap"),
                new Rockstar("Three", 3, "Rock"),
                new Popstar("Four", 1, "Pop")
        );

        assertThrows(IllegalArgumentException.class, () -> label.setLineUp(oversizedLineup));
    }

    @Test
    void applyStaminaToLineupAdjustsEachArtist() {
        Artist artistOne = new Popstar("Artist One", 1, "Pop");
        Artist artistTwo = new Rapper("Artist Two", 2, "Rap");
        Label label = new Label("Label", List.of(artistOne, artistTwo), new GameEnvironment());

        label.applyStaminaToLineup(-15);

        assertEquals(85, artistOne.getStamina());
        assertEquals(105, artistTwo.getStamina());
    }

    @Test
    void applyStaminaToLineupArtistWrapsIndexUsingFloorMod() {
        Artist artistOne = new Popstar("Artist One", 1, "Pop");
        Artist artistTwo = new Rapper("Artist Two", 2, "Rap");
        Label label = new Label("Label", List.of(artistOne, artistTwo), new GameEnvironment());

        label.applyStaminaToLineupArtist(-1, -20);

        assertEquals(100, artistOne.getStamina());
        assertEquals(100, artistTwo.getStamina());
    }

    @Test
    void applyStaminaToLineupArtistReturnsWhenLineupIsEmpty() {
        Label label = new Label("Label", new ArrayList<>(), new GameEnvironment());

        label.applyStaminaToLineupArtist(5, -10);

        assertTrue(label.getLineUp().isEmpty());
    }

    @Test
    void removeArtistRemovesArtistFromRosterAndLineup() {
        Artist artistOne = new Popstar("Artist One", 1, "Pop");
        Artist artistTwo = new Rapper("Artist Two", 2, "Rap");
        Label label = new Label("Label", List.of(artistOne, artistTwo), new GameEnvironment());

        label.removeArtist(artistOne);

        assertFalse(label.getAllArtists().contains(artistOne));
        assertFalse(label.getLineUp().contains(artistOne));
        assertTrue(label.getAllArtists().contains(artistTwo));
    }
}
