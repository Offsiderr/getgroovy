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
import seng201.team67.models.enums.questions.PayoutType;
import seng201.team67.models.minigames.MiniGameResult;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.services.gameplay.ConcertService;
import seng201.team67.services.gameplay.TourService;
import seng201.team67.services.setup.DifficultyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcertServiceTest {

    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private ConcertService concertService;
    private Artist firstArtist;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        firstArtist = new Popstar("Lead Pop", 1, "Pop");
        Artist secondArtist = new Rapper("Lead Rap", 3, "Rap");
        gameEnvironment.setLabel(new Label("Test Label", List.of(firstArtist, secondArtist), gameEnvironment));
        tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
        concertService = new ConcertService(gameEnvironment, tourService);
    }

    @Test
    @DisplayName("Resolving a concert event applies payout, crowd gain, and stamina drain")
    void handleAnswerAppliesPayoutCrowdGainAndStaminaDrain() {
        double startingCredits = gameEnvironment.getLabelService().getMoney();
        Outcome outcome = new Outcome(1, "Great encore", PayoutType.MAJOR_REWARD, -8, 10, false);

        concertService.handleAnswer(new Answer("Play the encore", List.of(outcome)));

        assertEquals(300, concertService.getIncome(), 0.0001);
        assertEquals(startingCredits + 300, gameEnvironment.getLabelService().getMoney(), 0.0001);
        assertEquals(92, firstArtist.getStamina());
        assertEquals(11, concertService.getCrowdEnergy());
        assertEquals(8, concertService.totalStaminaDrain(), 0.0001);
        assertEquals(1, tourService.getCurrentLineupStaminaIndex());
    }

    @Test
    @DisplayName("Concert ends when every generated event has been consumed")
    void getNextQuestionEndsConcertAfterFinalEvent() {
        for (int i = 0; i < gameEnvironment.getConfig().concertQuestionsCount; i++) {
            assertFalse(concertService.isEnded());
            assertTrue(concertService.getNextQuestion() != null);
        }

        assertNull(concertService.getNextQuestion());
        assertTrue(concertService.isEnded());
        assertTrue(tourService.getCreditsEarned() >= 0);
    }

    @Test
    @DisplayName("Ticket payout uses base ticket value, tour multiplier, crowd energy, and star power")
    void ticketRevenueUsesBaseValueAndMultipliers() {
        concertService.setCrowdEnergy(50);

        double revenue = concertService.calculateTicketRevenue();

        double starPowerMultiplier = 1 + gameEnvironment.getLabelService().getAverageSP()
                / gameEnvironment.getLabelService().getMaxSP();
        double expectedRevenue = 0.5
                * gameEnvironment.getConfig().ticketSalesAmount
                * TourType.LOCAL.getPayMultiplier()
                * starPowerMultiplier;
        assertEquals(expectedRevenue, revenue, 0.0001);
    }

    @Test
    @DisplayName("Applying a minigame result adjusts concert income and label credits")
    void miniGameResultAppliesCreditResultToConcertAndLabel() {
        double startingCredits = gameEnvironment.getLabelService().getMoney();

        concertService.applyMiniGameResult(new MiniGameResult(20, 125));

        assertEquals(20, concertService.getCrowdEnergy());
        assertEquals(125, concertService.getIncome(), 0.0001);
        assertEquals(startingCredits + 125, gameEnvironment.getLabelService().getMoney(), 0.0001);
    }
}
