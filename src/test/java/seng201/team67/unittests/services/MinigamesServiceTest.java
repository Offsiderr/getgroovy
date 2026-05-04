package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.models.enums.Minigame;
import seng201.team67.services.MinigamesService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MinigamesServiceTest {

    @Test
    void constructorInitialisesSoundEngineerMinigame() {
        MinigamesService service = new MinigamesService(Minigame.SOUNDENGINEER);

        assertEquals(Minigame.SOUNDENGINEER, service.minigame);
        assertNotNull(service.soundEngineerStandoff);
    }
}
