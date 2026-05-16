package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.models.GameConfig;
import seng201.team67.models.enums.TourType;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameConfigTest {

    @Test
    void easyPresetUsesExpectedStarterFriendlyValues() {
        GameConfig config = GameConfig.easy();

        assertAll(
                () -> assertEquals(500, config.startingCredits),
                () -> assertEquals(0.7, config.itemSellbackRate),
                () -> assertEquals(3, config.maxSPInStartingSelection),
                () -> assertEquals(0.25, config.randomEventTriggerChance),
                () -> assertEquals(5, config.concertQuestionsCount),
                () -> assertEquals(100, config.cancelTourPenalty),
                () -> assertEquals(25, config.concertCompletionScore),
                () -> assertEquals(5, config.questionAnsweredScore),
                () -> assertEquals(0.5, config.crowdHypeScoreMultiplier),
                () -> assertEquals(20.0, config.netEarningsScoreDivisor),
                () -> assertEquals(150, config.worldTourCompletionScore),
                () -> assertEquals(50, config.exhaustionScorePenalty),
                () -> assertEquals(15.0, config.soundEngineerMatchTolerance),
                () -> assertEquals(100.0, config.mainVolume),
                () -> assertEquals(100.0, config.musicVolume),
                () -> assertEquals(100.0, config.soundEffectsVolume),
                () -> assertEquals(1.0, config.getArtistPayMultiplier(TourType.LOCAL)),
                () -> assertEquals(1.5, config.getArtistPayMultiplier(TourType.COUNTRY)),
                () -> assertEquals(2.0, config.getArtistPayMultiplier(TourType.WORLD))
        );
    }

    @Test
    void harderPresetsReduceResourcesAndTightenMinigameTolerance() {
        GameConfig challenge = GameConfig.aChallenge();
        GameConfig hard = GameConfig.hard();

        assertAll(
                () -> assertEquals(300, challenge.startingCredits),
                () -> assertEquals(150, hard.startingCredits),
                () -> assertEquals(0.7, challenge.itemSellbackRate),
                () -> assertEquals(0.7, hard.itemSellbackRate),
                () -> assertEquals(0.30, challenge.randomEventTriggerChance),
                () -> assertEquals(0.40, hard.randomEventTriggerChance),
                () -> assertEquals(7, challenge.concertQuestionsCount),
                () -> assertEquals(9, hard.concertQuestionsCount),
                () -> assertEquals(30, challenge.concertCompletionScore),
                () -> assertEquals(35, hard.concertCompletionScore),
                () -> assertEquals(180, challenge.worldTourCompletionScore),
                () -> assertEquals(220, hard.worldTourCompletionScore),
                () -> assertEquals(10.0, challenge.soundEngineerMatchTolerance),
                () -> assertEquals(7.0, hard.soundEngineerMatchTolerance),
                () -> assertEquals(300, challenge.cancelTourPenalty),
                () -> assertEquals(500, hard.cancelTourPenalty),
                () -> assertEquals(75, challenge.exhaustionScorePenalty),
                () -> assertEquals(100, hard.exhaustionScorePenalty)
        );
    }
}
