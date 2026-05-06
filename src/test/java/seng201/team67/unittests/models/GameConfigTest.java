package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.models.GameConfig;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameConfigTest {

    @Test
    void easyPresetUsesExpectedStarterFriendlyValues() {
        GameConfig config = GameConfig.easy();

        assertAll(
                () -> assertEquals(500, config.startingCredits),
                () -> assertEquals(3, config.maxSPInStartingSelection),
                () -> assertEquals(5, config.concertQuestionsCount),
                () -> assertEquals(100, config.cancelTourPenalty),
                () -> assertEquals(15.0, config.soundEngineerMatchTolerance),
                () -> assertEquals(100.0, config.mainVolume),
                () -> assertEquals(100.0, config.musicVolume),
                () -> assertEquals(100.0, config.soundEffectsVolume)
        );
    }

    @Test
    void harderPresetsReduceResourcesAndTightenMinigameTolerance() {
        GameConfig challenge = GameConfig.aChallenge();
        GameConfig hard = GameConfig.hard();

        assertAll(
                () -> assertEquals(300, challenge.startingCredits),
                () -> assertEquals(150, hard.startingCredits),
                () -> assertEquals(7, challenge.concertQuestionsCount),
                () -> assertEquals(9, hard.concertQuestionsCount),
                () -> assertEquals(10.0, challenge.soundEngineerMatchTolerance),
                () -> assertEquals(7.0, hard.soundEngineerMatchTolerance),
                () -> assertEquals(300, challenge.cancelTourPenalty),
                () -> assertEquals(500, hard.cancelTourPenalty)
        );
    }
}
