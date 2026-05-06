package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MusicServiceTest {

    @Test
    void gameEnvironmentProvidesMusicService() {
        assertNotNull(new GameEnvironment().getMusicService());
    }
}
