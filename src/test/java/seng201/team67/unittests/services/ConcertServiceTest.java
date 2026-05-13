package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.behaviours.SkillBehaviours;
import seng201.team67.GameEnvironment;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.Skill;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.minigames.MiniGameResult;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.SkillEffects;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.PayoutType;
import seng201.team67.models.enums.TourType;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.services.gameplay.ConcertService;
import seng201.team67.services.gameplay.PayoutService;
import seng201.team67.services.gameplay.TourService;
import seng201.team67.services.setup.DifficultyService;

import java.lang.reflect.Field;
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
        Artist firstLineupArtist = gameEnvironment.getLabelService().getLineup().getFirst();

        Outcome outcome = new Outcome(1, "Gain momentum", PayoutType.GREAT_PAYOUT, -5, 10, false);
        Answer answer = new Answer("Choose this", List.of(outcome));

        service.handleAnswer(answer);

        assertEquals(new PayoutService().getPayoutAmount(gameEnvironment, PayoutType.GREAT_PAYOUT), service.getIncome(), 0.0001);
        assertEquals(95, firstLineupArtist.getStamina());
        assertEquals(1, tourService.getCurrentLineupStaminaIndex());
        assertEquals(11, service.getCrowdEnergyChange());
        assertEquals(5.0, service.totalStaminaDrain(), 0.0001);
    }

    @Test
    void handleAnswerAppliesSkillPayoutModifiersToConcertPayouts() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        gameEnvironment.getLabelService().getLineup().getFirst().setSkill(new Skill(
                "CHART_TOPPER",
                "Chart Topper",
                "Boosts payouts",
                "POPSTAR",
                Rarity.COMMON,
                List.of(SkillEffects.PAYOUT_MULTIPLIER),
                null,
                SkillBehaviours.payoutMultiplier(1.1)
        ));
        TourService tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
        ConcertService service = new ConcertService(gameEnvironment, tourService);

        Outcome outcome = new Outcome(1, "Gain momentum", PayoutType.OK_PAYOUT, 0, 0, false);
        Answer answer = new Answer("Choose this", List.of(outcome));

        service.handleAnswer(answer);

        assertEquals(110.0, service.getIncome(), 0.0001);
    }

    @Test
    void handleAnswerAppliesSkillStaminaReductionBeforeDrain() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        Artist firstLineupArtist = gameEnvironment.getLabelService().getLineup().getFirst();
        firstLineupArtist.setSkill(new Skill(
                "ROAD_VETERAN",
                "Road Veteran",
                "Reduces stamina drain",
                "ANY",
                Rarity.COMMON,
                List.of(SkillEffects.STAMINA_COST_REDUCTION),
                SkillBehaviours.staminaCostReduction(0.8),
                null
        ));
        TourService tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
        ConcertService service = new ConcertService(gameEnvironment, tourService);

        Outcome outcome = new Outcome(1, "Stay steady", PayoutType.NONE, -5, 0, false);
        Answer answer = new Answer("Choose this", List.of(outcome));

        service.handleAnswer(answer);

        assertEquals(96, firstLineupArtist.getStamina());
        assertEquals(4.0, service.totalStaminaDrain(), 0.0001);
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
    void concertOnlyOffersOneMinigameCheck() throws Exception {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        TourService tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
        ConcertService service = new ConcertService(gameEnvironment, tourService);
        setMiniGameTriggerChance(1.0);
        service.applyMiniGameResult(new MiniGameResult(50, 0));

        assertEquals(seng201.team67.models.enums.Minigame.SOUNDENGINEER, service.getConcertMinigame());
        assertNull(service.getConcertMinigame());
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

    @Test
    void createConcertResultsCapturesCurrentConcertSummary() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        TourService tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
        ConcertService service = new ConcertService(gameEnvironment, tourService);

        service.applyMiniGameResult(new MiniGameResult(20, 150));
        ConcertResults result = service.createConcertResults();

        assertEquals(service.calculateTicketRevenue(), result.ticketSales, 0.0001);
        assertEquals(150.0, result.bonusMoney, 0.0001);
        assertEquals(0.0, result.staminaChange, 0.0001);
        assertEquals(20, result.crowdHype);
        assertEquals(gameEnvironment.getLabelService().getLineupTotalPay(), result.artistsPay, 0.0001);
        assertEquals(result.ticketSales + result.bonusMoney - result.artistsPay, result.total, 0.0001);
    }

    @Test
    void ticketRevenueUsesTourPayMultiplier() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        ConcertService localService = new ConcertService(gameEnvironment, new TourService(new Tour(TourType.LOCAL), gameEnvironment));
        ConcertService worldService = new ConcertService(gameEnvironment, new TourService(new Tour(TourType.WORLD), gameEnvironment));

        localService.applyMiniGameResult(new MiniGameResult(20, 0));
        worldService.applyMiniGameResult(new MiniGameResult(20, 0));

        assertEquals(localService.calculateTicketRevenue() * TourType.WORLD.getPayMultiplier(),
                worldService.calculateTicketRevenue(),
                0.0001);
    }

    private GameEnvironment createEnvironmentWithLabel() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        gameEnvironment.setLabelName("Test Label");
        gameEnvironment.setLabel(new seng201.team67.models.Label("Test Label", List.of(
                new Popstar("Lead Pop", 1, "Pop"),
                new Rapper("Lead Rap", 3, "Rap")
        ), gameEnvironment));
        return gameEnvironment;
    }

    private void setMiniGameTriggerChance(double value) throws Exception {
        Field field = TourService.class.getDeclaredField("miniGameTriggerChance");
        field.setAccessible(true);
        field.set(null, value);
    }
}
