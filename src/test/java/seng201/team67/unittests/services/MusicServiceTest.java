package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.services.MusicService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MusicServiceTest {

    @Test
    void musicServiceCanBeConstructed() {
        assertNotNull(new MusicService(new GameEnvironment()));
    }
}
