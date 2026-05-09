package seng201.team67.services.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import seng201.team67.models.artists.Artist;

import java.io.InputStream;
import java.util.List;

public class ArtistLoaderService {

    private static final String resourcePath = "/data/artists.json";

    //We are using Jackson to load the Json file.
    public List<Artist> loadAll() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("artists.json not found at " + resourcePath);
            }
            return mapper.readValue(is, new TypeReference<List<Artist>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load artists: " + e.getMessage(), e);
        }
    }
}
