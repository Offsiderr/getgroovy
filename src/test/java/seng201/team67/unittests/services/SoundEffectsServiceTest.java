package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.GameConfig;
import seng201.team67.services.audio.SoundEffectsService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoundEffectsServiceTest {

    @Test
    void effectiveVolumeUsesMainAndSoundEffectsVolumes() {
        assertEquals(24.0, SoundEffectsService.calculateEffectiveVolume(40.0, 60.0));
    }

    @Test
    void mutedSoundEffectDoesNotRequireAudioResource() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        GameConfig config = GameConfig.easy();
        config.soundEffectsVolume = 0.0;
        gameEnvironment.setGameConfig(config);
        SoundEffectsService service = new SoundEffectsService(gameEnvironment);

        assertDoesNotThrow(service::playYes);
    }
}
