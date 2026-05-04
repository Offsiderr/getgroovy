package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.models.Artist;
import seng201.team67.services.ArtistLoaderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArtistLoaderServiceTest {

    @Test
    void loadAllReturnsArtistsFromJson() {
        List<Artist> artists = new ArtistLoaderService().loadAll();

        assertFalse(artists.isEmpty());
        assertTrue(artists.stream().allMatch(artist -> artist.getName() != null && !artist.getName().isBlank()));
        assertTrue(artists.stream().anyMatch(artist -> "Popstar".equals(artist.getType())));
        assertTrue(artists.stream().anyMatch(artist -> "Rapper".equals(artist.getType())));
        assertTrue(artists.stream().anyMatch(artist -> "Rockstar".equals(artist.getType())));
    }
}
