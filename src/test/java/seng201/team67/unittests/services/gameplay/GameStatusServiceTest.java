package seng201.team67.unittests.services.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.services.gameplay.GameStatusService;
import seng201.team67.services.setup.DifficultyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStatusServiceTest {

    private GameEnvironment gameEnvironment;
    private GameStatusService gameStatusService;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        gameStatusService = new GameStatusService();
    }

    @Test
    @DisplayName("Game is not lost while the label still has artists")
    void gameIsNotLostWhileRosterHasArtists() {
        gameEnvironment.setLabel(new Label("Test Label", List.of(new Popstar("Active", 1, "Pop")), gameEnvironment));

        assertFalse(gameStatusService.isGameLost(gameEnvironment));
    }

    @Test
    @DisplayName("Game is lost when there are no artists and no affordable recovery path")
    void gameIsLostWhenNoArtistsCanBeRecovered() {
        Artist expensiveArtist = new Popstar("Expensive", 5, "Pop");
        gameEnvironment.setLabel(new Label("Test Label", List.of(), gameEnvironment));
        gameEnvironment.setArtistPool(List.of(expensiveArtist));
        gameEnvironment.getLabelService().takeMoney(gameEnvironment.getLabelService().getMoney());

        assertTrue(gameStatusService.isGameLost(gameEnvironment));
    }

    @Test
    @DisplayName("Game is lost when money reaches the bankruptcy limit")
    void gameIsLostWhenMoneyReachesBankruptcyLimit() {
        gameEnvironment.setLabel(new Label("Test Label", List.of(new Popstar("Active", 1, "Pop")), gameEnvironment));
        gameEnvironment.getLabelService().takeMoney(
                gameEnvironment.getLabelService().getMoney() - GameStatusService.BANKRUPTCY_LIMIT);

        assertTrue(gameStatusService.isGameLost(gameEnvironment));
    }
}
