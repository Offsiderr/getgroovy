package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.GameConfig;
import seng201.team67.models.Label;
import seng201.team67.models.Tour;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.models.enums.PayoutType;
import seng201.team67.models.enums.TourType;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.gameplay.ConcertService;
import seng201.team67.services.gameplay.ScoreService;
import seng201.team67.services.gameplay.TourService;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreServiceTest {

    @Test
    void concertScoreUsesQuestionsCrowdAndNetEarnings() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        TourService tourService = new TourService(new Tour(TourType.LOCAL), gameEnvironment);
        ConcertService concertService = new ConcertService(gameEnvironment, tourService);
        ScoreService scoreService = new ScoreService();

        for (int i = 0; i < 3; i++) {
            concertService.getNextQuestion();
        }

        ConcertResults results = new ConcertResults(120.0, 50.0, 4.0, 40, 30.0, 140.0);

        assertEquals(67, scoreService.calculateConcertScore(gameEnvironment, concertService, results));
    }

    @Test
    void successfulTourAwardsConfiguredCompletionBonus() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        TourService tourService = new TourService(new Tour(TourType.COUNTRY), gameEnvironment);
        ScoreService scoreService = new ScoreService();

        assertEquals(90, scoreService.calculateTourScore(gameEnvironment, tourService));
    }

    @Test
    void exhaustedTourAppliesPenaltyToCompletionBonus() {
        GameEnvironment gameEnvironment = createEnvironmentWithLabel();
        TourService tourService = new TourService(new Tour(TourType.WORLD), gameEnvironment);
        ScoreService scoreService = new ScoreService();

        tourService.endTourDueToExhaustion();

        assertEquals(100, scoreService.calculateTourScore(gameEnvironment, tourService));
    }

    private GameEnvironment createEnvironmentWithLabel() {
        GameEnvironment gameEnvironment = instantiateGameEnvironment();
        gameEnvironment.setDifficulty(Difficulty.EASY);
        gameEnvironment.setGameConfig(GameConfig.easy());
        gameEnvironment.setLabelName("Test Label");
        List<Question> questionPool = List.of(new Question(
                "score-test",
                "Test prompt",
                List.of(new Answer("Continue", List.of(new Outcome(1, "No-op", PayoutType.NONE, 0, 0, false))))
        ));
        gameEnvironment.setCommonQuestionPool(questionPool);
        gameEnvironment.setLocalQuestionPool(questionPool);
        gameEnvironment.setCountryQuestionPool(questionPool);
        gameEnvironment.setWorldQuestionPool(questionPool);
        gameEnvironment.setLabel(new Label("Test Label", List.of(
                new Popstar("Lead Pop", 1, "Pop"),
                new Rapper("Lead Rap", 3, "Rap")
        ), gameEnvironment));
        return gameEnvironment;
    }

    private GameEnvironment instantiateGameEnvironment() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);
            return (GameEnvironment) unsafe.allocateInstance(GameEnvironment.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
