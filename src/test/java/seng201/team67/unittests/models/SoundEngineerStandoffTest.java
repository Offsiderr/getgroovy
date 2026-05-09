package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.models.minigames.SoundEngineerStandoff;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoundEngineerStandoffTest {

    @Test
    void countMatchesCountsExactAndWithinToleranceValues() {
        SoundEngineerStandoff minigame = new SoundEngineerStandoff();

        long matches = minigame.countMatches(
                List.of(10.0, 24.5, 35.0, 80.0),
                List.of(10.0, 20.0, 40.0, 70.0),
                5.0
        );

        assertEquals(3, matches);
    }

    @Test
    void countMatchesReturnsZeroWhenAllValuesMissTolerance() {
        SoundEngineerStandoff minigame = new SoundEngineerStandoff();

        long matches = minigame.countMatches(
                List.of(0.0, 10.0, 20.0),
                List.of(50.0, 60.0, 70.0),
                4.0
        );

        assertEquals(0, matches);
    }
}
