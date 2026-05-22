package seng201.team67.unittests.services.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.enums.RandomEvent;
import seng201.team67.services.gameplay.RandomEventService;
import seng201.team67.services.setup.DifficultyService;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomEventServiceTest {

    private GameEnvironment gameEnvironment;
    private RandomEventService randomEventService;
    private Artist artist;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        artist = new Popstar("Solo", 2, "Pop");
        gameEnvironment.setLabel(new Label("Test Label", List.of(artist), gameEnvironment));
        randomEventService = new RandomEventService(new Random(1));
    }

    @Test
    @DisplayName("Random event trigger helper follows the configured probability boundary")
    void triggerHelperFollowsConfiguredProbabilityBoundary() {
        int triggered = 0;
        int iterations = 1_000;

        for (int i = 0; i < iterations; i++) {
            if (randomEventService.shouldTriggerRandomEvent(gameEnvironment.getConfig(), i / (double) iterations)) {
                triggered++;
            }
        }

        assertEquals(250, triggered);
    }

    @Test
    @DisplayName("Stat random events apply their change to the affected artist")
    void statRandomEventAppliesChangeToAffectedArtist() {
        artist.setStamina(60);

        boolean applied = randomEventService.applyRandomEvent(gameEnvironment, RandomEvent.GYM_ARC, artist);

        assertTrue(applied);
        assertEquals(75, artist.getStamina());
    }

    @Test
    @DisplayName("Retirement events cannot remove the label's last remaining artist")
    void retirementEventDoesNotRemoveLastRemainingArtist() {
        boolean applied = randomEventService.applyRandomEvent(gameEnvironment, RandomEvent.AMICABLE_EXIT, artist);

        assertFalse(applied);
        assertEquals(List.of(artist), gameEnvironment.getLabelService().getAllArtists());
    }
}
