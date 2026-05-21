package seng201.team67.services.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import seng201.team67.models.artists.Artist;

import java.io.InputStream;
import java.util.List;

/**
 * Provides artist loader operations for the game. Uses Jackson to load the Artists.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ArtistLoaderService {

    /** The resource path. */
    private static final String resourcePath = "/data/artists.json";

    /**
     * Loads all the artists found at the artist.json file.
     * @return A list containing all the artists found at the artist.json file.
     */
    public List<Artist> loadAll() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("artists.json not found at " + resourcePath);
            }
            /**
             * Represents the value used by the game.
             * @author Louie Campion
             * @author Keenan Aubrey
             */
            return mapper.readValue(is, new TypeReference<List<Artist>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load artists: " + e.getMessage(), e);
        }
    }
}
