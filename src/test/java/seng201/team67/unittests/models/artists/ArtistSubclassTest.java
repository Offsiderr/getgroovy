package seng201.team67.unittests.models.artists;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ArtistSubclassTest {

    @Test
    void concreteArtistSubclassesUseTheirOwnTypeAndBaseStamina() {
        Popstar popstar = new Popstar("Ariana Grande", 3, "Pop artist");
        Rapper rapper = new Rapper("Kendrick Lamar", 4, "Rap artist");
        Rockstar rockstar = new Rockstar("Hayley Williams", 2, "Rock artist");

        assertAll(
                () -> assertEquals("Popstar", popstar.getType()),
                () -> assertEquals(100, popstar.getBaseStamina()),
                () -> assertEquals("Rapper", rapper.getType()),
                () -> assertEquals(120, rapper.getBaseStamina()),
                () -> assertEquals("Rockstar", rockstar.getType()),
                () -> assertEquals(80, rockstar.getBaseStamina())
        );
    }

    @Test
    void jacksonTypeFieldDeserializesToConcreteArtistSubclass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Artist artist = mapper.readValue("""
                {
                  "type": "RAP",
                  "name": "MF DOOM",
                  "starPower": 5,
                  "description": "Rap artist"
                }
                """, Artist.class);

        assertInstanceOf(Rapper.class, artist);
        assertEquals("Rapper", artist.getType());
        assertEquals(120, artist.getBaseStamina());
    }
}
