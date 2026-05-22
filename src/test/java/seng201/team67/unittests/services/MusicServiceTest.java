package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.GameConfig;
import seng201.team67.services.audio.MusicService;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MusicServiceTest {

    @Test
    void gameEnvironmentProvidesMusicService() {
        MusicService service = new GameEnvironment().getMusicService();
        assertNotNull(service);
    }

    @Test
    void effectiveVolumeUsesMainAndMusicVolumes() {
        assertEquals(30.0, MusicService.calculateEffectiveVolume(60.0, 50.0));
    }

    @Test
    void stopAndResetClearsCurrentTrackAndPlayingState() throws ReflectiveOperationException {
        GameEnvironment gameEnvironment = new GameEnvironment();
        GameConfig config = GameConfig.easy();
        config.musicVolume = 0.0;
        gameEnvironment.setGameConfig(config);
        MusicService service = gameEnvironment.getMusicService();

        service.playTheStudioMusic();
        service.stopAndReset();

        assertFalse((Boolean) readField(service, "currentlyPlaying"));
        assertNull(readField(service, "currentTrack"));
    }

    private Object readField(Object target, String fieldName) throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}
