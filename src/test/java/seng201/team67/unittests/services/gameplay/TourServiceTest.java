package seng201.team67.unittests.services.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.Label;
import seng201.team67.models.Tour;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.gameplay.TourService;
import seng201.team67.services.setup.DifficultyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TourServiceTest {

    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private Artist activeArtist;
    private Artist reserveArtist;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        activeArtist = new Popstar("Active", 1, "Pop");
        Artist secondArtist = new Rapper("Second", 2, "Rap");
        reserveArtist = new Popstar("Reserve", 1, "Pop");
        gameEnvironment.setLabel(new Label("Test Label", List.of(activeArtist, secondArtist, reserveArtist), gameEnvironment));
        gameEnvironment.getLabelService().setLineUp(List.of(activeArtist, secondArtist));
        tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
    }

    @Test
    @DisplayName("Tour end increments active artists and resets reserve artists")
    void tourEndIncrementsActiveArtistCountersAndResetsReserveCounters() {
        reserveArtist.incrementConsecutiveToursWithoutBreak();
        reserveArtist.incrementConsecutiveToursWithoutBreak();

        tourService.tourEnded();

        assertEquals(1, activeArtist.getConsecutiveToursWithoutBreak());
        assertEquals(0, reserveArtist.getConsecutiveToursWithoutBreak());
    }

    @Test
    @DisplayName("Tour detects when every lineup artist has exhausted stamina")
    void detectsWhenAllLineupArtistsHaveNoStamina() {
        gameEnvironment.getLabelService().getLineup().forEach(artist -> artist.setStamina(0));

        assertTrue(tourService.isLineupExhausted());
    }

    @Test
    @DisplayName("Ending a tour due to exhaustion charges the remaining-concert penalty once")
    void endTourDueToExhaustionChargesRemainingConcertPenaltyOnce() {
        double startingCredits = gameEnvironment.getLabelService().getMoney();
        tourService.addCreditsEarned(500.0);
        tourService.increaseStopIndex();

        tourService.endTourDueToExhaustion();
        tourService.endTourDueToExhaustion();

        double expectedPenalty = 2 * gameEnvironment.getConfig().getCancelTourPenalty(TourType.LOCAL);
        assertTrue(tourService.isEndedByExhaustion());
        assertEquals(expectedPenalty, tourService.getExhaustionRefund(), 0.0001);
        assertEquals(500 - expectedPenalty, tourService.getCreditsEarned(), 0.0001);
        assertEquals(startingCredits - expectedPenalty, gameEnvironment.getLabelService().getMoney(), 0.0001);
    }

    @Test
    @DisplayName("Cancelling a tour early returns false when the label cannot afford the penalty")
    void cancelTourEarlyReturnsFalseWhenPenaltyCannotBeAfforded() {
        gameEnvironment.getLabelService().takeMoney(gameEnvironment.getLabelService().getMoney());

        assertFalse(tourService.canCancelTourEarly());
        assertFalse(tourService.cancelTourEarly());
        assertEquals(0, tourService.getCancellationRefund(), 0.0001);
    }
}
