package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.services.audio.SoundEffectsService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoundEffectsServiceTest {

    @Test
    void calculateEffectiveVolumeCombinesMasterAndEffectsLevels() {
        assertEquals(25.0, SoundEffectsService.calculateEffectiveVolume(50.0, 50.0));
    }

    @Test
    void calculateEffectiveVolumeClampsOutOfRangeValues() {
        assertEquals(0.0, SoundEffectsService.calculateEffectiveVolume(-20.0, 60.0));
        assertEquals(100.0, SoundEffectsService.calculateEffectiveVolume(150.0, 120.0));
    }

    @Test
    void playHandlesMissingResourceGracefully() {
        assertDoesNotThrow(() -> SoundEffectsService.play("/sound/sfx/missing.wav"));
    }

    @Test
    void yesAndNoHelpersDoNotThrow() {
        SoundEffectsService service = new SoundEffectsService(new GameEnvironment());

        assertDoesNotThrow(service::playYes);
        assertDoesNotThrow(service::playNo);
    }
}
