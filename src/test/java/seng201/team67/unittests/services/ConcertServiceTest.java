package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.Artist;
import seng201.team67.models.MiniGameResult;
import seng201.team67.models.Popstar;
import seng201.team67.models.Rapper;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.PayoutType;
import seng201.team67.models.enums.TourType;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.services.ConcertService;
import seng201.team67.services.TourService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcertServiceTest {

    @Test
    void calculateCrowdGainUsesLineupStarPower() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        ConcertService service = new ConcertService(gameEnvironment, new TourService(new Tour(TourType.LOCAL), gameEnvironment));

        double crowdGain = service.calculateCrowdGain(10);

        assertEquals(11.6667, crowdGain, 0.0001); //Sometimes the double calculations are a little off, and this was failing tests ocassionally, so I've added in deltas to give it a little leway
    }

    @Test
    void handleAnswerAppliesPayoutStaminaAndCrowdChanges() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        TourService tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
        ConcertService service = new ConcertService(gameEnvironment, tourService);
        double startingMoney = gameEnvironment.getLabelService().getMoney();
        Artist firstLineupArtist = gameEnvironment.getLabelService().getLineup().getFirst();

        Outcome outcome = new Outcome(1, "Gain momentum", PayoutType.GREAT_PAYOUT, -5, 10, false);
        Answer answer = new Answer("Choose this", List.of(outcome));

        service.handleAnswer(answer);

        assertEquals(startingMoney + gameEnvironment.getPayoutAmount(PayoutType.GREAT_PAYOUT), gameEnvironment.getLabelService().getMoney(), 0.0001);
        assertEquals(95, firstLineupArtist.getStamina());
        assertEquals(1, tourService.getCurrentLineupStaminaIndex());
        assertEquals(11, service.getCrowdEnergyChange());
        assertEquals(5.0, service.totalStaminaDrain(), 0.0001);
    }

    @Test
    void applyMiniGameResultUpdatesIncomeAndCrowdMeter() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        ConcertService service = new ConcertService(gameEnvironment, new TourService(new Tour(TourType.LOCAL), gameEnvironment));

        service.applyMiniGameResult(new MiniGameResult(15, 200));

        assertEquals(15, service.getCrowdEnergyChange());
        assertEquals(200.0, service.getIncome(), 0.0001);
    }

    @Test
    void getNextQuestionReturnsNullAfterConcertEnds() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        TourService tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
        ConcertService service = new ConcertService(gameEnvironment, tourService);

        for (int i = 0; i < gameEnvironment.getConfig().concertQuestionsCount; i++) {
            assertFalse(service.isEnded());
            assertTrue(service.getNextQuestion() != null);
        }

        assertNull(service.getNextQuestion());
        assertTrue(service.isEnded());
    }

    private GameEnvironment createEnvironmentWithLabel() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        gameEnvironment.setDifficulty(0);
        gameEnvironment.setLabelName("Test Label");
        gameEnvironment.createLabel(List.of(
                new Popstar("Lead Pop", 1, "Pop"),
                new Rapper("Lead Rap", 3, "Rap")
        ));
        return gameEnvironment;
    }
}
