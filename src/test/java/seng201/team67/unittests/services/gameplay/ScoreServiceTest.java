package seng201.team67.unittests.services.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.Label;
import seng201.team67.models.Tour;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.gameplay.ConcertService;
import seng201.team67.services.gameplay.ScoreService;
import seng201.team67.services.gameplay.TourService;
import seng201.team67.services.setup.DifficultyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreServiceTest {

    private GameEnvironment gameEnvironment;
    private ScoreService scoreService;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        gameEnvironment.setLabel(new Label(
                "Test Label",
                List.of(new Popstar("Lead", 1, "Pop")),
                gameEnvironment
        ));
        scoreService = new ScoreService();
    }

    @Test
    @DisplayName("Applying concert score adds the calculated score to the game total")
    void applyConcertScoreAddsToGameTotal() {
        ConcertService concertService = new ConcertService(
                gameEnvironment,
                new TourService(new Tour(TourType.LOCAL), gameEnvironment)
        );
        concertService.setAnsweredQuestionCountForDebug(3);
        ConcertResults results = new ConcertResults(100.0, 50.0, 5.0, 80, 20.0, 130.0);

        int score = scoreService.applyConcertScore(gameEnvironment, concertService, results);

        assertEquals(score, gameEnvironment.getGameScore());
        assertEquals(87, score);
    }
}
