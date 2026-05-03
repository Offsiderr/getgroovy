package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.services.SoundEffectsService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SoundEffectsServiceTest {

    @Test
    void playHandlesMissingResourceGracefully() {
        assertDoesNotThrow(() -> SoundEffectsService.play("/sound/sfx/missing.wav"));
    }

    @Test
    void yesAndNoHelpersDoNotThrow() {
        SoundEffectsService service = new SoundEffectsService();

        assertDoesNotThrow(service::playYes);
        assertDoesNotThrow(service::playNo);
    }
}
